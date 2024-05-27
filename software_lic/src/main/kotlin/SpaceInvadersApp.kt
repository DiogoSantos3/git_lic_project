import isel.leic.utils.Time

object SpaceInvadersApp {
    private var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    private var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    private var score : Int = 0
    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    private var line = 0
    private val row = 1
    private var shootingKey = ' ' // Inicializa a tecla de tiro
    private var hit = false

    val interval = 1000L // Intervalo de tempo para gerar novos números aleatórios (1000 ms)
    var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    // Função para inicializar os componentes
    fun init() {
        HAL.init()
        KBD.init()
        LCD.init()
        SerialEmitter.init()
        ScoreDisplay.init()
        LCD.write(" Space Invaders ") // Escreve a mensagem inicial na primeira linha do LCD
        LCD.cursor(1, 0)
        LCD.write(" Game X  X X  0$ ") // Escreve uma segunda mensagem na segunda linha do LCD
    }




    fun gameOver(score: Int) {// Função para exibir a mensagem de fim de jogo
        LCD.clear() // Limpa o LCD
        LCD.cursor(0, 0)
        LCD.write("*** GAME OVER **") // Mensagem de fim de jogo na primeira linha
        LCD.cursor(1, 0)
        LCD.write("Score: $score ") // Exibe a pontuação na segunda linha
        LCD.cursor(1, 17)
    }

    fun addScore(score: Int):Int{
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de x.
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        return score + 1
    }

    private fun addInvaders(randomLine:Int, randomNumber:String, hit:Boolean){
        if (randomLine == 0) {
            list0 += randomNumber
            TUI.displayWrite(list0, list1, randomLine, 1, hit)
        } else {
            list1 += randomNumber
            TUI.displayWrite(list0, list1, randomLine, 1, hit)
        }
    }


    // Função principal do jogo
    fun playing() {

        TUI.displayBars() // Exibe as barras no início das linhas
        TUI.showGun(line,row)


        while (list0.length < 14 && list1.length <14) {// Loop principal do jogo

            val currentTime = System.currentTimeMillis() //Tempo atual

            // Verifica se o intervalo de tempo foi atingido, ou seja, se já passou 1 segundo
            if (currentTime - lastUpdateTime >= interval) {
                randomLine = (0..1).random()
                randomNumber = (0..9).random().toString()

                TUI.displayWrite(list0, list1, line, 1, hit) // Atualiza o display com os números atuais
                hit = false


                addInvaders(randomLine,randomNumber,hit)//Adiciona o número aleatório à linha correspondente (Adiciona um invader)
                lastUpdateTime = currentTime // Atualiza o tempo da última atualização
            }

            when (val key = KBD.getKey()) { // Verifica se alguma tecla foi pressionada// Lida com a tecla pressionada

                '*' -> {//Verifica se queremos mudar de linha do cursor

                    LCD.cursor(line,row)
                    LCD.write(" ")

                    line = if (line == 0) 1 else 0 // Alterna entre as linhas 0 e 1
                    TUI.showGun(line, row)
                    LCD.cursor(line, row)

                }

                '#' -> {//Verifica se matou um invader
                    if (line == 0) {// Verifica se há um 'hit' na linha 0
                        if (list0.isNotEmpty() && shootingKey == list0[0]) {
                            score = addScore(score)
                            list0 = list0.substring(1) // Remove o primeiro número da lista
                            hit = true
                            TUI.displayWrite(list0, list1, line, 1, hit) // Atualiza o display com os números atuais
                        }
                    } else {// Verifica se há um 'hit' na linha 1
                        if (list1.isNotEmpty() && shootingKey == list1[0]) {
                            score = addScore(score)
                            list1 = list1.substring(1) // Remove o primeiro número da lista
                            hit = true
                            TUI.displayWrite(list0, list1, line, 1, hit) // Atualiza o display com os números atuais
                        }
                    }
                }

                else -> {// Exibe a tecla pressionada no LCD
                    if (line == 0 && key != KBD.NONE) {
                        LCD.cursor(line, 0)
                        LCD.write(key)
                        shootingKey = key

                    } else if (key != KBD.NONE) {
                        LCD.cursor(line, 0)
                        LCD.write(key)
                        shootingKey = key
                    }
                }
            }
            Time.sleep(100) // Pequeno delay para não sobrecarregar a CPU
        }
        gameOver(score * 10) // Exibe a tela de fim de jogo com a pontuação 0
    }
}



// Função principal para iniciar o jogo
fun main() {
    SpaceInvadersApp.init()
    while (true) {
        if (KBD.getKey() == '*') { // Espera pela tecla '*' para iniciar o jogo
            LCD.clear()
            SpaceInvadersApp.playing() // Inicia o jogo
            break
        }
    }
}
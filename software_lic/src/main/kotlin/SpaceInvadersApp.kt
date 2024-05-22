import isel.leic.utils.Time

object SpaceInvadersApp {
    private var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    private var list1: String = "" // Lista para armazenar números aleatórios na linha 1

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

    // Função para exibir barras nas primeiras posições das duas linhas
    private fun displayBars() {
        LCD.cursor(0, 0)
        LCD.write("]")
        LCD.cursor(1, 0)
        LCD.write("]")
    }

    // Função para exibir a mensagem de fim de jogo
    fun gameOver(score: Int) {
        LCD.clear() // Limpa o LCD
        LCD.cursor(0, 0)
        LCD.write("*** GAME OVER **") // Mensagem de fim de jogo na primeira linha
        LCD.cursor(1, 0)
        LCD.write("Score: $score ") // Exibe a pontuação na segunda linha
        LCD.cursor(1, 17)
    }

    // Função para escrever as listas de números no LCD
    fun escreve(list0: String, list2: String, line: Int, row: Int, hit: Boolean) {
        displayBars() // Exibe as barras no início das linhas
        val maxLength = 17
        val startingPosition1 = maxLength - (list0.length + 1)
        val startingPosition2 = maxLength - (list2.length + 1)

        // Lógica para exibir os números na posição correta, dependendo da linha
        if (line == 0) {
            LCD.cursor(line, startingPosition1)
            LCD.write(list0)

            LCD.cursor(line + 1, startingPosition2)
            LCD.write(list2)

            // Se um 'hit' ocorrer, limpa a linha e exibe as barras novamente
            if (hit) {
                LCD.cursor(line, 1)
                LCD.write(' ')
                LCD.clear()
                displayBars()
            }
        } else {
            LCD.cursor(line - 1, startingPosition1)
            LCD.write(list0)

            LCD.cursor(line, startingPosition2)
            LCD.write(list2)

            // Se um 'hit' ocorrer, limpa a linha e exibe as barras novamente
            if (hit) {
                LCD.cursor(line, 1)
                LCD.write(' ')
                LCD.clear()
                displayBars()
            }
        }
    }

    // Função principal do jogo
    fun playing() {
        displayBars() // Exibe as barras no início das linhas
        var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
        var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

        var line = 0
        val row = 1
        var shootingKey = ' ' // Inicializa a tecla de tiro
        var hit = false

        val interval = 1000L // Intervalo de tempo para gerar novos números aleatórios (700 ms)
        var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização

        // Loop principal do jogo
        while (list0.length < 14 || list1.length < 14) {
            val currentTime = System.currentTimeMillis()
            // Verifica se o intervalo de tempo foi atingido
            if (currentTime - lastUpdateTime >= interval) {
                randomLine = (0..1).random()
                randomNumber = (0..9).random().toString()

                escreve(list0, list1, line, 1, hit) // Atualiza o display com os números atuais
                hit = false

                // Adiciona o número aleatório à linha correspondente
                if (randomLine == 0) {
                    list0 += randomNumber
                    escreve(list0, list1, randomLine, 1, hit)
                } else {
                    list1 += randomNumber
                    escreve(list0, list1, randomLine, 1, hit)
                }

                lastUpdateTime = currentTime // Atualiza o tempo da última atualização
            }

            val key = KBD.getKey() // Verifica se alguma tecla foi pressionada

            // Lida com a tecla pressionada
            when (key) {
                '*' -> {
                    line = if (line == 0) 1 else 0 // Alterna entre as linhas 0 e 1
                    LCD.cursor(line, row)
                }
                '#' -> {
                    // Verifica se há um 'hit' na linha 0
                    if (line == 0) {
                        if (list0.isNotEmpty() && shootingKey == list0[0]) {
                            list0 = list0.substring(1) // Remove o primeiro número da lista
                            LCD.cursor(line, row)
                            hit = true
                        }
                    } else {
                        // Verifica se há um 'hit' na linha 1
                        if (list1.isNotEmpty() && shootingKey == list1[0]) {
                            list1 = list1.substring(1) // Remove o primeiro número da lista
                            LCD.cursor(line, row)
                            hit = true
                        }
                    }
                }
                else -> {
                    // Exibe a tecla pressionada no LCD
                    if (line == 0 && key != KBD.NONE) {
                        LCD.cursor(line, row)
                        LCD.write(key)
                        shootingKey = key
                    } else if (key != KBD.NONE) {
                        LCD.cursor(line, row)
                        LCD.write(key)
                        shootingKey = key
                    }
                }
            }

            Time.sleep(100) // Pequeno delay para não sobrecarregar a CPU
        }
        gameOver(0) // Exibe a tela de fim de jogo com a pontuação 0
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
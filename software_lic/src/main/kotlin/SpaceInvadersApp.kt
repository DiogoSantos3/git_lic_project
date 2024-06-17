import isel.leic.utils.Time
import java.io.File

object SpaceInvadersApp {

    enum class State{MANUTENCION, INITIAL, PLAYING, GAMEOVER}

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    var state = State.INITIAL

    private const val SHOW_INVADERS_INTERVAL = 200L // Intervalo de tempo para gerar novos números aleatórios (200 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    public var cursor = TUI.Cursor()
    var bestScore = TUI.bestScore

    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de score.

        return score + 1
    }
    fun init() {// Função para inicializar os componentes
        TUI.init()
        ScoreDisplay.init()
    }
    fun updateTime() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime >= SHOW_INVADERS_INTERVAL) {
            randomLine = (0..1).random()
            randomNumber = (0..9).random().toString()
            TUI.addInvaders(randomLine, randomNumber) // Adiciona invasores aleatórios
            lastUpdateTime = currentTime
        }
    }
    fun playing() {//Função principal do jogo

        cursor.displayBars() // Exibe as barras no início das linhas
        cursor.showGun(cursor.line, cursor.row) // Exibe a posição inicial da arma
        while (TUI.list0.length < 14 && TUI.list1.length < 14) { // Loop principal do jogo
            updateTime()
            TUI.handleKeyPress()
            Time.sleep(100) // Delay
        }

        TUI.gameOver(TUI.score)

    }
    private const val NAME_FILE = "statistics.txt"
    private val text = File(NAME_FILE).readLines()
    private  var num_of_players = text.count()
    private val statistics = mutableListOf<Pair<String, Int>>()//Lista de pares (nome, pontuação)
    fun displayStatistics():Boolean {

        if(state == State.INITIAL){

            statistics.clear() //Limpa a lista de estatísticas antes de começar a preencher novamente


            for (entry in text) { //Processa cada entrada de texto para extrair nome e pontuação
                val parts = entry.split(";") // Divide a string
                val name = parts[0]
                val score = parts[1].toInt()
                statistics.add(Pair(name, score)) //Add Pair(name, score) à lista (statistics)
            }

            statistics.sortByDescending { it.second }//Ordena a lista por ordem decrescente

            var position = 1 //Variável para rastrear a posição na lista

            for ((name, score) in statistics) { //Exibir cada Pair(name, score)

                if(CoinAccepter.isCoin()){
                    println("COIN")}

                if (KBD.getKey() == '*') {//Verifica se queremos iniciar o jogo
                    SpaceInvadersApp.state = SpaceInvadersApp.State.PLAYING
                    break // Sai da função
                }


                cursor.write(1, 0, "$position-$name    $score     ") //Escreve a pontuação


                if (num_of_players == position) {//Verifica ja percorreu a a lista toda e reseta a position
                    position = 0
                }
                position++
                Time.sleep(700)

                // Loop para verificação de moedas durante a espera
                for (i in 1..10) {

                    if(CoinAccepter.isCoin()){
                        println("COIN")}
                    Time.sleep(200)// Espera 200 ms antes de próxima verificação
                }
            }
        }
        return false
    }

    }




fun main() {
    var state = SpaceInvadersApp.state
    SpaceInvadersApp.init()
    TUI.cursor.initialDisplay()
    Time.sleep(2000)

    while (true) {

        when (SpaceInvadersApp.state){
                       SpaceInvadersApp.State.INITIAL ->{
                           SpaceInvadersApp.displayStatistics()

                       }
            SpaceInvadersApp.State.PLAYING ->{
                SpaceInvadersApp.playing()

            }
            SpaceInvadersApp.State.MANUTENCION ->{
                println("X")

            }
            SpaceInvadersApp.State.GAMEOVER ->{
                println("X")

            }



        }



    }


}
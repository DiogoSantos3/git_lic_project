import isel.leic.utils.Time
import kotlin.system.exitProcess

object SpaceInvadersApp {

    //========== VARIÁVEIS ==========

    enum class State { MANUTENCION, INITIAL, PLAYING, GAMEOVER }

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    var state = State.INITIAL

    var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    var score: Int = 0
    var shootingKey = ' ' // Inicializa a tecla de tiro
    private var hit = false

    private const val SHOW_INVADERS_INTERVAL = 100L // Intervalo de tempo para gerar novos números aleatórios
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    private var cursor = TUI.Cursor()
    private val topScore = Scores.getTopScore()

    //===================================================
    fun init() {// Função para inicializar os componentes
        TUI.init()
        ScoreDisplay.init()
    }

    private fun scoreOnOff(){
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(300)
        ScoreDisplay.off(false) // Desliga o display de pontuação.

    }
    private fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 10) // Atualiza o display com o valor atual de score.
        return score + 10
    }

    private fun changeLine() {
        cursor.displayBars()
        var linee = cursor.line
        cursor.write(linee, cursor.row, " ")
        linee = if (cursor.line == 0) 1 else 0
        cursor.showGun(linee, cursor.row)
        cursor = TUI.Cursor(linee, cursor.row)
    }
    private fun checkHit() {

        if (cursor.line == 0 && list0.isNotEmpty() && shootingKey == list0[0]) {
            score = SpaceInvadersApp.addScore(score)
            list0 = list0.substring(1)
            TUI.displayWrite(list0.drop(0), list1, cursor.line, 1, hit = true)

        } else if (cursor.line == 1 && list1.isNotEmpty() && shootingKey == list1[0]) {
            score = SpaceInvadersApp.addScore(score)
            list1 = list1.substring(1)
            TUI.displayWrite(list0, list1.drop(0), cursor.line, 1, hit = true)
        }

    }

    private fun updateTime() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime >= SHOW_INVADERS_INTERVAL) {
            randomLine = (0..1).random()
            randomNumber = (0..9).random().toString()
            addInvaders(randomLine, randomNumber) // Adiciona invasores aleatórios
            lastUpdateTime = currentTime
        }
    }
    private fun displayKey(key: Char, line: Int) {
        if (key != TUI.NONE) {
            cursor.write(line, 0,key.toString())
            shootingKey = key
        }
    }

    fun playing() {//Função principal do jogo

        cursor.displayBars() // Exibe as barras no início das linhas
        cursor.showGun(cursor.line, cursor.row) // Exibe a posição inicial da arma
        while (list0.length < 14 && list1.length < 14) { // Loop principal do jogo
            updateTime()

            when (val key : Char = TUI.getKey()) {
                '*' -> changeLine()
                '#' -> checkHit()
                else -> displayKey(key,cursor.line)
            }

            Time.sleep(100)//Delay
        }

        Statistics.addGames()//TIRA COINS AQUI??? OU NO GAMEOVER???
        Statistics.addCoins(false)//TIRA COINS AQUI??? OU NO GAMEOVER???
        list0 = ""
        list1 = ""
        state = State.GAMEOVER

    }

    fun manutencion() {
        var ack = false
        while (M.isM()) {
            cursor.write(0, 0, " On Maintenance ")
            cursor.write(1, 0, "*-Count  #-ShutD  ")
            while (true) {
                val key: Char = TUI.getKey()
                if (key in '0'..'9') {
                    println("Starting game without coin.")
                    TUI.clear()
                    state = State.PLAYING
                    return
                }
                when(key){
                    '*' -> {
                        displayStats()
                        while (true) {
                            val innerKey: Char = TUI.getKey()

                            if (innerKey in '0'..'9') {

                                state = State.MANUTENCION
                                return
                            }
                            else if (innerKey == '*') {
                                state = State.MANUTENCION
                                return
                            }
                            else if (innerKey == '#') {
                                cursor.write(0, 0, " Reset Counting?         ") // MUDAR
                                cursor.write(1, 0, " 5-Yes other-No  ") // MUDAR
                                while (true) {
                                    val shutdownKey: Char = TUI.getKey()
                                    if (shutdownKey == '5') {
                                        Statistics.resetCounting()
                                        state = State.MANUTENCION
                                        return
                                    } else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') {
                                        state = State.MANUTENCION
                                        return
                                    }
                                }
                            }
                        }
                    }
                    '#' -> {
                        cursor.write(0, 0, "    Shut Down?         ") // MUDAR
                        cursor.write(1, 0, " 5-Yes other-No   ") // MUDAR
                        while (true) {
                            val shutdownKey: Char = KBD.getKey()
                            if (shutdownKey == '5') {
                                exitProcess(0)

                            }
                            else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') {
                                state = State.MANUTENCION
                                return
                            }
                        }
                    }


                }






            }
        }
    }

    private fun displayStats() {
        cursor.write(0, 0, " Coins:${Statistics.numCoins()}          ")
        cursor.write(1, 0, " Games:${Statistics.numGames()}         ")
    }

    fun displayStatistics() {

        if (state == State.INITIAL) {
            score = 0
            ScoreDisplay.setScore(score)
            val cores = Scores.splitScores().take(20)
            var position = 1 //Variável para rastrear a posição na lista

            for ((name, score) in cores) { //Exibir cada Pair(name, score)
                val numCoins = Statistics.numCoins().toInt()

                if (M.isM()) {
                    state = State.MANUTENCION
                    break
                }

                if (CoinAccepter.isCoin()) {
                    Statistics.addCoins(true)
                    if (Statistics.numCoins().toInt() <= 9) {
                        cursor.write(1, 0, " Game X  X X  ${Statistics.numCoins()}$    ")
                        Time.sleep(1500)
                    } else {
                        cursor.write(1, 0, " Game X  X X ${Statistics.numCoins()}$    ")
                        Time.sleep(1500)
                    }

                }

                if (TUI.getKey() == '*' && numCoins >= 2) {//Verifica se queremos iniciar o jogo
                    TUI.clear()
                    state = State.PLAYING
                    break // Sai da função
                }


                TUI.cursor.write(0, 0, " Space Invaders ")
                cursor.write(1, 0, "${position}-${name}   $score                   ") //Escreve a pontuação
                Time.sleep(1200)
                if (Statistics.numCoins().toInt() <= 9) {
                    cursor.write(1, 0, " Game X  X X  ${Statistics.numCoins()}$    ")
                    Time.sleep(1000)
                } else {
                    cursor.write(1, 0, " Game X  X X ${Statistics.numCoins()}$    ")
                    Time.sleep(1000)
                }


                if (Scores.num_of_players == position ) {//Verifica ja percorreu a a lista toda e reseta a position
                    position = 0
                }

                position++

                println(Statistics.numCoins())

                // Loop para verificação de moedas durante a espera
                for (i in 1..5) {
                    if (M.isM()) {
                        state = State.MANUTENCION
                        break
                    }
                    if (CoinAccepter.isCoin()) {
                        Statistics.addCoins(true)
                        if (Statistics.numCoins().toInt() <= 9) {
                            cursor.write(1, 0, " Game X  X X  ${Statistics.numCoins()}$    ")
                            Time.sleep(1200)

                        } else {
                            cursor.write(1, 0, " Game X  X X ${Statistics.numCoins()}$    ")
                            Time.sleep(1200)
                        }

                    }
                    Time.sleep(100)// Espera 200 ms antes de próxima verificação
                }
            }
        }

    }

    private fun addInvaders(randomLine: Int, randomNumber: String) {

        if (randomLine == 0) {
            list0 += randomNumber
        } else {
            list1 += randomNumber
        }
        TUI.displayWrite(list0, list1, randomLine, 1, hit)
    }


    fun gameOover() {
        cursor.write(0, 0, "*** GAME OVER ***")
        cursor.write(1, 0, " Score: ${SpaceInvadersApp.score}            ")

        if (score < topScore?.toInt()!!) {
            scoreOnOff()
            state = State.INITIAL
            return

        } else {
            scoreOnOff()
            val newName = TUI.newScore() //quero guardar um novo nome

            Scores.writePlayers(newName, (score).toString())
            state = State.INITIAL
            return
        }


    }

}

fun main() {

    SpaceInvadersApp.init()
    TUI.cursor.write(0,0," Space Invaders ")
    TUI.cursor.write(1,0," Game X  X X ${Statistics.numCoins()}$    ")
    Time.sleep(2000)

    while (true) {
        println(SpaceInvadersApp.state)
        when (SpaceInvadersApp.state){
            SpaceInvadersApp.State.INITIAL ->{
                SpaceInvadersApp.displayStatistics()
            }

            SpaceInvadersApp.State.PLAYING ->{
                SpaceInvadersApp.playing()
            }
            SpaceInvadersApp.State.MANUTENCION ->{
                SpaceInvadersApp.manutencion()

            }
            SpaceInvadersApp.State.GAMEOVER ->{
                SpaceInvadersApp.gameOover()

            }
        }
    }
}
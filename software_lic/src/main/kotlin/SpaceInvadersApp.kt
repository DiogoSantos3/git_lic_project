import isel.leic.utils.Time

object SpaceInvadersApp {

    enum class State { MANUTENCION, INITIAL, PLAYING, GAMEOVER }

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    var state = State.INITIAL

    private const val SHOW_INVADERS_INTERVAL = 1000L // Intervalo de tempo para gerar novos números aleatórios (200 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    var cursor = TUI.Cursor()
    var bestScore = TUI.bestScore
    val topScore = Scores.getTopScore()

    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 10) // Atualiza o display com o valor atual de score.
        return score + 10
    }

    fun init() {// Função para inicializar os componentes
        TUI.init()
        ScoreDisplay.init()
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

    fun playing() {//Função principal do jogo

        cursor.displayBars() // Exibe as barras no início das linhas
        cursor.showGun(cursor.line, cursor.row) // Exibe a posição inicial da arma
        while (list0.length < 14 && list1.length < 14) { // Loop principal do jogo
            updateTime()
            println(score)
            TUI.handleKeyPress(cursor.line)
            Time.sleep(100) // Delay
        }

        state = State.GAMEOVER

    }

    fun manutencion() {
        var ack = false
        while (M.isM()) {
            when (TUI.man()) {
                "MANUTENCION" -> {
                    Time.sleep(100)
                    ack = false
                    continue
                }
                "PLAYING" -> {
                    Time.sleep(100)
                    state = State.PLAYING
                    ack = true
                    break
                }
                "INITIAL" -> {
                    Time.sleep(100)
                    state = State.INITIAL
                    ack = false
                    break
                }
            }
        }
        state = if (ack) State.PLAYING else State.INITIAL
    }


    fun displayStats() {

        cursor.write(0, 0, " Coins:${Statistics.numCoins()}          ")
        cursor.write(1, 0, " Games:${Statistics.numGames()}         ")
    }

    fun displayStatistics() {

        if (state == State.INITIAL) {

            val cores = Scores.splitScores()
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
                    } else {
                        cursor.write(1, 0, " Game X  X X ${Statistics.numCoins()}$    ")
                    }

                }

                if (TUI.readyToPlay() && numCoins >= 2) {//Verifica se queremos iniciar o jogo
                    state = State.PLAYING
                    break // Sai da função
                }

                TUI.cursor.write(0, 1, "Space Invaders ")
                cursor.write(1, 0, "${position}-${name}         $score     ") //Escreve a pontuação


                if (Scores.num_of_players == position) {//Verifica ja percorreu a a lista toda e reseta a position
                    position = 0
                }

                position++
                Time.sleep(700)


                // Loop para verificação de moedas durante a espera
                for (i in 1..10) {
                    if (M.isM()) {
                        state = State.MANUTENCION
                        break
                    }
                    if (CoinAccepter.isCoin()) {
                        Statistics.addCoins(true)
                        if (Statistics.numCoins().toInt() <= 9) {
                            cursor.write(1, 0, " Game X  X X  ${Statistics.numCoins()}$    ")
                        } else {
                            cursor.write(1, 0, " Game X  X X ${Statistics.numCoins()}$    ")
                        }

                    }
                    Time.sleep(200)// Espera 200 ms antes de próxima verificação
                }
            }
        }

    }

    var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    var score: Int = 0
    var shootingKey = ' ' // Inicializa a tecla de tiro
    var hit = false
    fun changeLine() {
        cursor.displayBars()
        var linee = cursor.line
        cursor.write(linee, cursor.row, " ")
        linee = if (cursor.line == 0) 1 else 0
        cursor.showGun(linee, cursor.row)
        cursor = TUI.Cursor(linee, cursor.row)
    }

    fun checkHit() {

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

    fun addInvaders(randomLine: Int, randomNumber: String) {

        if (randomLine == 0) {
            list0 += randomNumber
        } else {
            list1 += randomNumber
        }
        TUI.displayWrite(list0, list1, randomLine, 1, hit)
    }

    fun gameOver(): String { //Função para exibir a mensagem de fim de jogo

        cursor.write(0, 0, "*** GAME OVER **")
        cursor.write(1, 0, "Score: ${SpaceInvadersApp.score}            ")

        if (SpaceInvadersApp.score > bestScore) {
            Time.sleep(1500)
            return "NEWSCORE"
        }
        return "NONEWSCORE"
    }

    fun gameOover() {
        cursor.write(0, 0, "*** GAME OVER **")
        cursor.write(1, 0, "Score: ${SpaceInvadersApp.score}            ")
        if (score < bestScore) {
            Statistics.addGames()
            Statistics.addCoins(false)
            state = State.INITIAL
            return

        } else {
            val newName = TUI.newScore() //quero guardar um novo nome
            Statistics.addGames()
            Statistics.addCoins(false)
            Scores.writePlayers(newName, (score).toString())
            state = State.INITIAL
            return
        }


    }

}

fun main() {

    SpaceInvadersApp.init()
    TUI.cursor.write(0,1,"Space Invaders ")
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
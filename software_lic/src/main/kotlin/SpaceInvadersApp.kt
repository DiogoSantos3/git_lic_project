import isel.leic.utils.Time

object SpaceInvadersApp {

    enum class State{MANUTENCION, INITIAL, PLAYING, GAMEOVER}

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    var state = State.INITIAL

    private const val SHOW_INVADERS_INTERVAL = 200L // Intervalo de tempo para gerar novos números aleatórios (200 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

     var cursor = TUI.Cursor()
    var bestScore = TUI.bestScore

    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de score.
        return score + 1
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

        state = State.GAMEOVER

    }

    fun manutencion(){
        var ack = false
        while (M.isM()) {
            if (TUI.man() == "MANUTENCION"){
                Time.sleep(100)
                ack = false
                continue}
            else if(TUI.man() == "PLAYING") {
                Time.sleep(100)
                state = State.PLAYING
                ack = true
                break
                }
            else if (TUI.man() == "INITIAL"){
                Time.sleep(100)
                state =  State.INITIAL
                ack = false
                break

                }
        }
        if (ack){state=  State.PLAYING}
         else{state =  State.INITIAL}


    }
    fun displayStats() {

        cursor.write(0, 0, " Coins:${Statistics.numCoins()}          ")
        cursor.write(1, 0, " Games:${Statistics.numGames()}         ")
    }


    fun displayStatistics() {

        if(state == State.INITIAL){

            val cores = Scores.splitScores()
            var position = 1 //Variável para rastrear a posição na lista

            for ((name, score) in cores) { //Exibir cada Pair(name, score)
                val numCoins = Statistics.numCoins().toInt()

                if (M.isM()){
                    state = State.MANUTENCION
                    break
                }

                if(CoinAccepter.isCoin()){
                    cursor.write(1,0," Game X  X X  ${numCoins}$    ")
                    Statistics.addCoins(Statistics.numCoins().toInt())
                }

                if (TUI.readyToPlay() && numCoins >= 2) {//Verifica se queremos iniciar o jogo
                    state = State.PLAYING
                    break // Sai da função
                }

                TUI.cursor.write(0,1,"Space Invaders ")
                cursor.write(1, 0, "$position-$name    $score     ") //Escreve a pontuação


                if (Scores.num_of_players == position) {//Verifica ja percorreu a a lista toda e reseta a position
                    position = 0
                }

                position++
                Time.sleep(700)

                // Loop para verificação de moedas durante a espera
                for (i in 1..10) {
                    if (M.isM()){
                        state = State.MANUTENCION
                        break
                    }
                    if(CoinAccepter.isCoin()){
                        cursor.write(1,0," Game X  X X  ${Statistics.numCoins()}$    ")
                        Statistics.addCoins(Statistics.numCoins().toInt())}
                    Time.sleep(200)// Espera 200 ms antes de próxima verificação
                }
            }
        }

    }

    fun gameOover(){
        if (TUI.gameOver() == "NONEWSCORE"){//Não há bestScore (argumento)

            state = State.INITIAL
        }

        else if (TUI.gameOver() == "NEWSCORE"){
                val newName = TUI.newScore() //quero guardar um novo nome
                Scores.writePlayers(newName, (TUI.score*10).toString())
                state = State.INITIAL

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
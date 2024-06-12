import isel.leic.utils.Time
import java.io.File

object SpaceInvadersApp {

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    private const val SHOW_INVADERS_INTERVAL = 1000L // Intervalo de tempo para gerar novos números aleatórios (200 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    public var cursor = TUI.Cursor()
    var bestScore = TUI.bestScore

    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de score.
        return score + 1
    }

    fun init() { // Função para inicializar os componentes
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

    fun playing() { // Função principal do jogo
        cursor.displayBars() // Exibe as barras no início das linhas
        cursor.showGun(cursor.line, cursor.row) // Exibe a posição inicial da arma
        while (TUI.list0.length < 14 && TUI.list1.length < 14) { // Loop principal do jogo
            if (M.isM()) {
                x()
                return
            }
            updateTime()
            TUI.handleKeyPress()
            Time.sleep(100) // Delay
        }

        TUI.gameOver(TUI.score)
    }

    fun x() {
        //RESETAR
        //
        LCD.clear()

    }
}

fun main() {
    while (true) {
        if (M.isM()) {
            SpaceInvadersApp.x()
            while (M.isM()) {
                // Wait until the switch is OFF
                Time.sleep(200)
            }
        }
        else {
            var readyToPlay = false
            SpaceInvadersApp.init()
            TUI.cursor.initialDisplay(-1)
            Time.sleep(2000)

            while (!readyToPlay) {
                if (M.isM()) {
                    SpaceInvadersApp.x()
                    break
                }

                readyToPlay = TUI.displayStatistics(readyToPlay)
                CoinAccepter.isCoin()

                if (TUI.readyToPlay()) readyToPlay = true
            }

            if (readyToPlay) {
                LCD.clear()
                SpaceInvadersApp.playing()
            }
        }
    }
}
import isel.leic.utils.Time

object SpaceInvadersApp {


    private var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    private var list1: String = "" // Lista para armazenar números aleatórios na linha 1


     var score: Int = 0

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    private var shootingKey = ' ' // Inicializa a tecla de tiro
    private var hit = false

    private const val SHOW_INVADERS_INTERVAL = 1000L // Intervalo de tempo para gerar novos números aleatórios (1000 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)


    public var cursor = TUI.Cursor()
    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de x.
        return score + 1
    }

    // Função para inicializar os componentes
    fun init() {
        TUI.init()
        ScoreDisplay.init()

        cursor.write(0,1,"Space Invaders ")//SAIUBDGIWUBA
        cursor.write(1,0,"Game X  X X  ${TUI.coin} ")//SAIUBDGIWUBA
        Time.sleep(2000)
    }



    fun addInvaders(randomLine: Int, randomNumber: String) {
        if (randomLine == 0) {
            list0 += randomNumber
        } else {
            list1 += randomNumber
        }
        TUI.displayWrite(list0, list1, randomLine, 1, hit)
    }

    fun changeLine() {

        var linee = cursor.line
        cursor.write(linee,cursor.row," ")
        linee = if (cursor.line == 0) 1 else 0
        TUI.showGun(linee, cursor.row)
        cursor = TUI.Cursor(linee,cursor.row)
    }

    fun checkHit() {

        if (cursor.line == 0 && list0.isNotEmpty() && shootingKey == list0[0]) {
            score = addScore(score)
            list0 = list0.substring(1)
            TUI.displayWrite(list0, list1, cursor.line, 1, hit = true)

        } else if (cursor.line == 1 && list1.isNotEmpty() && shootingKey == list1[0]) {
            score = addScore(score)
            list1 = list1.substring(1)
            TUI.displayWrite(list0, list1, cursor.line, 1, hit = true)
        }
    }

    fun displayKey(key: Char) {
        if (key != KBD.NONE) {
            cursor.write(cursor.line, 0,key.toString())
            shootingKey = key
        }
    }

    fun handleKeyPress(key: Char) {
        when (key) {
            '*' -> changeLine()
            '#' -> checkHit()
            else -> displayKey(key)
        }
    }
    fun updateTime() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime >= SHOW_INVADERS_INTERVAL) {
            randomLine = (0..1).random()
            randomNumber = (0..9).random().toString()
            addInvaders(randomLine, randomNumber) //Add random invaders
            lastUpdateTime = currentTime
        }
    }


    // Função principal do jogo
    fun playing() : Boolean {
        TUI.displayBars() // Exibe as barras no início das linhas
        TUI.showGun(cursor.line, cursor.row)
        while (list0.length < 14 && list1.length < 14) { // Loop principal do jogo
            updateTime()
            handleKeyPress(KBD.getKey())
            Time.sleep(100) //Delay
        }
        TUI.gameOver(SpaceInvadersApp.score * 10)
       return false

    }
}

// Função principal para iniciar o jogo
fun main() {
    SpaceInvadersApp.init()


    while ( true) {
        //TUI.displayStatistics()

        TUI.isCoin()


        if (KBD.getKey() == '*') {
            break
        }
    }
    LCD.clear()

    SpaceInvadersApp.playing()



}

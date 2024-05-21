
import isel.leic.utils.Time
import java.io.File

object SpaceInvadersApp {
    private const val NAME_FILE = "statistics.txt"
    private const val INITIAL_DELAY = 3000L
    private const val SCORE_DISPLAY_TIME = 5000L
    private const val INVADERS_SPEED = 1000L
    private const val SCORE_PADDING = 14
    private const val WAITING_KEY_TIME = 0L
    var lineCursor = 0
    var rowCursor = 0
    var score : Int = 0

    fun init() {

        HAL.init()
        KBD.init()
        LCD.init()
        SerialEmitter.init()
        ScoreDisplay.init()
    }

    fun start() {
        LCD.write(" Space Invaders ")
        LCD.cursor(1, 0)
        LCD.write(" Game X  X X  0$ ")
        Time.sleep(INITIAL_DELAY)

        while (true) {
            val text = File(NAME_FILE).readLines()
            for (i in text) {
                LCD.cursor(1, 0)
                LCD.write(" ${i}         ")
                if (waitForKey(WAITING_KEY_TIME)) return
            }
            if (waitForKey(WAITING_KEY_TIME)) break
        }
        playing()
    }

    fun gameOver(score: Int) {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("*** GAME OVER **")
        LCD.cursor(1, 0)
        LCD.write("Score: $score ")
        LCD.cursor(1, 17)

    }


    val key: MutableList<Char> = mutableListOf()
    fun playing() {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("]")
        LCD.cursor(1, 0)
        LCD.write("]")
        LCD.cursor(lineCursor, 1)
        Time.sleep(INITIAL_DELAY)

        var list1 = ""
        var list2 = ""
        var actualLine = 0




        while (true) {
            val x = KBD.getKey()
            rowCursor = 2
            val randomNumber = (0..9).random()
            val randomCursor = (0..1).random()



            if (key.isEmpty()){
                LCD.write(x)
                key.add(x)

            }

            while (key.isNotEmpty()){

            }

            /*
                        if (randomCursor == 0) {

                            lineCursor = randomCursor

                            list1 += randomNumber.toString()
                            //if (updateDisplay(list1, lineCursor,rowCursor)) break
                        }
                        else {

                            lineCursor = randomCursor

                            list2 += randomNumber.toString()
                            //if (updateDisplay(list2, lineCursor,rowCursor)) break
                        }


             */


            Time.sleep(INVADERS_SPEED)
        }
        //gameOver(score)
    }

    private fun updateDisplay(list: String,line:Int, row: Int): Boolean {
        val paddedList = list.padStart(SCORE_PADDING)
        LCD.cursor(line,row)
        LCD.write(paddedList)
        return paddedList.trim().length == SCORE_PADDING
    }

    private fun waitForKey(time: Long): Boolean {
        var elapsed = 0L
        val interval = 10L
        while (elapsed < SCORE_DISPLAY_TIME) {
            if (KBD.waitKey(WAITING_KEY_TIME) == '*') {

                playing()
                return true
            }
            Time.sleep(interval)
            elapsed += interval
        }
        return false
    }
}




private var nColUP = 1
private var nColDOWN = 1
private var actualColumn = Column.UP

enum class Column { UP, DOWN }


fun writeKey(time: Long) {
    var temp = KBD.waitKey(time)//Obtem a tecla premida
    while (temp != KBD.NONE) {//Enquando a tecla não for nenhuma...
        when (temp) {
            '#' -> {//Se a tecla premida for '#', faz clear do display e volta a obter a tecla

                nColUP = 0                          //Coluna 0 fica com 0 números
                nColDOWN = 0                        //Coluna 1 fica com 0 números
            }

            '*' -> {
                if (actualColumn == Column.UP) {    //Se line == True (está no cursor de cima), muda para o de baixo
                    LCD.cursor(1, nColDOWN)
                    actualColumn = Column.DOWN
                } else {                            //Se line == False (está no cursor de baixo), muda para o de cima
                    LCD.cursor(0, nColUP)
                    actualColumn = Column.UP
                }
            }

        }
        temp = KBD.waitKey(time)
    }

}


fun main() {
    SpaceInvadersApp.init()
    SpaceInvadersApp.playing()
    //SpaceInvadersApp.start()
    //SpaceInvadersApp.showInvaders()
    //SpaceInvadersApp.test()
    //SpaceInvadersApp.gameOver(144)
}
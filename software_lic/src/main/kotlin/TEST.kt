
/*
import isel.leic.utils.Time
import java.io.File


object SpaceInvadersApp {
    private const val NAME_FILE = "statistics.txt"
    private const val INITIAL_DELAY = 3000L
    private const val SCORE_DISPLAY_TIME = 5000L
    private const val DISPLAY_WIDTH = 16
    private const val SCORE_PADDING = 14

    fun init() {
        HAL.init()
        KBD.init()
        LCD.init()
        SerialEmitter.init()
        ScoreDisplay.init()
    }

    fun start(time: Long) {
        LCD.write(" Space Invaders ")
        LCD.cursor(1, 0)
        LCD.write(" Game X  X X  0$ ")
        Time.sleep(INITIAL_DELAY)

        while (true) {
            val text = File(NAME_FILE).readLines()
            for (i in text) {
                LCD.cursor(1, 0)
                LCD.write(" ${i}         ")
                if (waitForKey(time)) return
            }
            if (waitForKey(time)) break
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

    fun playing() {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("]")
        LCD.cursor(1, 0)
        LCD.write("]")

        var list1 = ""
        var list2 = ""

        while (true) {
            val randomNumber = (0..9).random()
            val randomCursor = (0..1).random()

            if (randomCursor == 0) {
                list1 += randomNumber.toString()
                if (updateDisplay(list1, 0)) break
            } else {
                list2 += randomNumber.toString()
                if (updateDisplay(list2, 1)) break
            }

            Time.sleep(500)
        }
        gameOver(122)
    }

    private fun updateDisplay(list: String, row: Int): Boolean {
        val paddedList = list.padStart(SCORE_PADDING)
        LCD.cursor(row, 2)
        LCD.write(paddedList)
        return paddedList.trim().length == SCORE_PADDING
    }

    private fun waitForKey(time: Long): Boolean {
        var elapsed = 0L
        val interval = 10L
        while (elapsed < SCORE_DISPLAY_TIME) {
            if (KBD.waitKey(1) == '*') {
                playing()
                return true
            }
            Time.sleep(interval)
            elapsed += interval
        }
        return false
    }
}

fun main() {
    SpaceInvadersApp.init()
    //SpaceInvadersApp.playing()
    SpaceInvadersApp.start(0)
    //SpaceInvadersApp.showInvaders()
    //SpaceInvadersApp.test()
    //SpaceInvadersApp.gameOver(144)
}
*/
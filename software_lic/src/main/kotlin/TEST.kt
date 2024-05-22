import isel.leic.utils.Time
import java.io.File

object paceInvadersApp {
    private var list1: String = ""
    private var list2: String = ""

    fun init() {
        HAL.init()
        KBD.init()
        LCD.init()
        SerialEmitter.init()
        ScoreDisplay.init()
        LCD.write(" Space Invaders ")
        LCD.cursor(1, 0)
        LCD.write(" Game X  X X  0$ ")
    }

    private fun displayBars() {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("]")
        LCD.cursor(1, 0)
        LCD.write("]")
    }

    fun gameOver(score: Int) {
        LCD.clear()
        LCD.cursor(0, 0)
        LCD.write("*** GAME OVER **")
        LCD.cursor(1, 0)
        LCD.write("Score: $score ")
        LCD.cursor(1, 17)
    }

    fun writeeKey() {
        var key = KBD.getKey() // Obtem a tecla premida
        while (list1.length < 14 && list2.length < 14) { // Enquanto a tecla não for '5'...
            val randomNumber = (0..9).random().toString()
            val randomLine = (0..1).random()
            Time.sleep(1000)
            var keyProcessed = false
            when (key) {
                '#' -> {
                    println("REMOVE")
                    if (randomLine == 0) {
                        if (list1.isNotEmpty()) {
                            list1 = list1.substring(1)
                            LCD.clear()
                            TUI.displayWrite(list1, randomLine, list1.length -2)
                            LCD.cursor(1,list2.length -1)
                            TUI.displayWrite(list2, randomLine, list2.length -1)
                            println("List1 (REMOVED) = $list1")
                        }
                    } else {
                        if (list2.isNotEmpty()) {
                            list2 = list2.substring(1)
                            LCD.clear()
                            TUI.displayWrite(list2, randomLine, list2.length - 2)

                            TUI.displayWrite(list1, randomLine, list1.length - 1)
                            println("List2 (REMOVED) = $list2")
                        }
                    }
                    keyProcessed = true
                }

                '*' -> {
                    println("XXXX")
                    keyProcessed = true
                }

                else -> {
                    // Outras teclas não precisam ser processadas
                }
            }

            if (!keyProcessed) {
                if (randomLine == 0) {
                    // Adiciona o número à lista1 e escreve no display
                    list1 += randomNumber
                    TUI.displayWrite(randomNumber, randomLine, list1.length - 1)
                    println("List1 = $list1")
                } else {
                    // Adiciona o número à lista2 e escreve no display
                    list2 += randomNumber
                    TUI.displayWrite(randomNumber, randomLine, list2.length - 1)
                    println("List2 = $list2")
                }
            }

            key = KBD.getKey()
        }
    }

    fun playing() {
        LCD.clear()
        displayBars()

        while (list1.length < 14 && list2.length < 14) {
            writeeKey()
        }
        gameOver(0)
    }
}

fun main() {
    paceInvadersApp.init()
    paceInvadersApp.playing()
}
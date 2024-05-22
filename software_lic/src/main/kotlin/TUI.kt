
object TUI {
    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP

    enum class Column { UP, DOWN }

    var col0 = 16
    var col1 = 16
    fun displayWrite(data: String, line: Int, col: Int) {
        val maxLength = 16  // A largura máxima do display
        val startingPosition = maxLength - (col + 1)

        LCD.cursor(line, startingPosition)
        LCD.write(data)
    }



    fun writeKey(time: Long) {
        var temp = KBD.waitKey(time)//Obtem a tecla premida
        while (temp != KBD.NONE) {//Enquando a tecla não for nenhuma...
            when (temp) {
                '#' -> {//Se a tecla premida for '#', faz clear do display e volta a obter a tecla
                    LCD.clear()
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

                else -> {//Senão escreve no LCD a tecla premida aumentando o número de números nas colunas
                    LCD.write(temp)
                    if (actualColumn == Column.UP) {
                        nColUP++
                    } else {
                        nColDOWN++
                    }
                }
            }
            temp = KBD.waitKey(time)
        }

    }

}

fun main() {
    HAL.init()
    KBD.init()
    LCD.init()
    SerialEmitter.init()
    ScoreDisplay.init()

    LCD.clear()
    LCD.cursor(0, 0)
    LCD.write("test")
    TUI.writeKey(99999999999999999)

}

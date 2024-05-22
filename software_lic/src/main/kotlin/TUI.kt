
object TUI {
    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP

    enum class Column { UP, DOWN }

    var col0 = 16
    var col1 = 16

    // Função para escrever as listas de números no LCD
    fun displayWrite(list0: String, list2: String, line: Int, row: Int, hit: Boolean) {
        displayBars() // Exibe as barras no início das linhas
        val maxLength = 17
        val startingPosition1 = maxLength - (list0.length + 1)
        val startingPosition2 = maxLength - (list2.length + 1)

        // Lógica para exibir os números na posição correta, dependendo da linha
        if (line == 0) {
            LCD.cursor(line, startingPosition1)
            LCD.write(list0)

            LCD.cursor(line + 1, startingPosition2)
            LCD.write(list2)

            // Se um 'hit' ocorrer, limpa a linha e exibe as barras novamente
            if (hit) {
                LCD.cursor(line, 1)
                LCD.write(' ')
                LCD.clear()
                displayBars()
            }
        } else {
            LCD.cursor(line - 1, startingPosition1)
            LCD.write(list0)

            LCD.cursor(line, startingPosition2)
            LCD.write(list2)

            // Se um 'hit' ocorrer, limpa a linha e exibe as barras novamente
            if (hit) {
                LCD.cursor(line, 1)
                LCD.write(' ')
                LCD.clear()
                displayBars()
            }
        }
    }
     fun displayBars() {
        LCD.cursor(0, 0)
        LCD.write("]")
        LCD.cursor(1, 0)
        LCD.write("]")
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

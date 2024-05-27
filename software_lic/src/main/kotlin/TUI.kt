
object TUI {
    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP

    enum class Column { UP, DOWN }

    var col0 = 16
    var col1 = 16
    fun showGun(line:Int,row:Int){
        LCD.cursor(line,row)
        LCD.write(">")
    }

    // Função para escrever as listas de números no LCD
    fun displayWrite(list0: String, list1: String, line: Int, row: Int, hit: Boolean) {

        val maxLength = 17
        val startingPosition0 = maxLength - (list0.length + 1)
        val startingPosition1 = maxLength - (list1.length + 1)

        // Se um 'hit' ocorrer, limpa apenas a posição do hit e reexibe a linha
        if (hit) {
            if (line == 0) {

                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                LCD.clear()
                displayBars()
                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX

                LCD.cursor(0, startingPosition0)
                LCD.write(list0)
                LCD.cursor(1, startingPosition1)
                LCD.write(list1)

            } else {

                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                LCD.clear()
                displayBars()
                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                LCD.cursor(0, startingPosition0)
                LCD.write(list0)
                LCD.cursor(1, startingPosition1)
                LCD.write(list1)
            }
            showGun(line, row)
        }

        else{// Atualiza ambas as linhas independentemente da linha atual
            LCD.cursor(0, startingPosition0)
            LCD.write(list0)
            LCD.cursor(1, startingPosition1)
            LCD.write(list1)}



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

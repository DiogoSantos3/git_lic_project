object TUI {

    var nColUP = 0
    var nColDOWN = 0
    var actualColumn = column.UP

    enum class column { UP, DOWN }


    fun writeKey(time: Long) {

        var x = KBD.waitKey(time)                   //Obtem a tecla premida
        while (x != KBD.NONE) {                     //Enquando a tecla não for nenhuma...

            if (x == '#') {                         //Se a tecla premida for '#', faz clear do display e volta a obter a tecla
                LCD.clear()
                x = KBD.waitKey(time)
                nColUP = 0                          //Coluna 0 fica com 0 números
                nColDOWN = 0                        //Coluna 1 fica com 0 números

            } else if (x == '*') {                  //Se a tecla premida for '*', muda de cursor
                if (actualColumn == column.UP) {    //Se line == True (está no cursor de cima), muda para o de baixo
                    LCD.cursor(1, nColDOWN)
                    x = KBD.waitKey(time)
                    actualColumn = column.DOWN
                } else {                            //Se line == False (está no cursor de baixo), muda para o de cima
                    LCD.cursor(0, nColUP)
                    x = KBD.waitKey(time)
                    actualColumn = column.UP
                }

            } else {                                //Senão escreve no LCD a tecla premida aumentando o número de números nas colunas
                LCD.write(x)
                x = KBD.waitKey(time)
                if (actualColumn == column.UP) {
                    nColUP++
                } else {
                    nColDOWN++
                }
            }
        }
    }
}


fun main() {
    HAL.init()
    KBD.init()
    LCD.init()
    LCD.clear()
    LCD.cursor(0, 0)
    TUI.writeKey(99999999999999999)
}

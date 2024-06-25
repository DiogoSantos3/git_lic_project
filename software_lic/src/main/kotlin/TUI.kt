    import isel.leic.utils.Time
    import java.io.File
    import kotlin.system.exitProcess

    object TUI {
        var list0: String = "" // Lista para armazenar números aleatórios na linha 0
        var list1: String = "" // Lista para armazenar números aleatórios na linha 1
        private var nColUP = 0
        private var nColDOWN = 0
        private var actualColumn = Column.UP
        private const val NAME_FILE = "scores.txt"
        private val text = File(NAME_FILE).readLines()

        enum class Column { UP, DOWN }
        var cursor = Cursor()
        private var shootingKey = ' ' // Inicializa a tecla de tiro
        var score: Int = 0
        private var hit = false
        var bestScore : Int = 0
        private var letter = listOf('A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z')

        class Cursor(val line: Int = 0,val row: Int = 1){
            fun write(line: Int,row: Int,word:String){
                LCD.cursor(line,row)
                LCD.write(word) // Escreve a mensagem inicial na primeira linha do
            }
            fun showGun(line:Int,row:Int){
                cursor.write(line,row,">")
            }

            fun displayBars() {
                cursor.write(0,0,"]")
                cursor.write(1,0,"]")
            }
        }

        fun init(){
            LCD.init()
            KBD.init()
        }
        fun handleKeyPress() {
            when (val key : Char = KBD.getKey()) {
                '*' -> changeLine()
                '#' -> checkHit()
                else -> displayKey(key)
            }
        }
        private fun checkHit() {

            if (cursor.line == 0 && list0.isNotEmpty() && shootingKey == list0[0]) {
                score = SpaceInvadersApp.addScore(score)
                list0 = list0.substring(1)
                displayWrite(list0,  list1, cursor.line, 1, hit = true)

            } else if (cursor.line == 1 && list1.isNotEmpty() && shootingKey == list1[0]) {
                score =  SpaceInvadersApp.addScore(score)
                list1 =  list1.substring(1)
                displayWrite(list0,  list1, cursor.line, 1, hit = true)
            }
        }
        private fun changeLine() {

            var linee = cursor.line
            cursor.write(linee,cursor.row," ")
            linee = if (cursor.line == 0) 1 else 0
            cursor.showGun(linee, cursor.row)
            cursor = Cursor(linee,cursor.row)
        }
        private fun displayKey(key: Char) {
            if (key != KBD.NONE) {
                cursor.write(cursor.line, 0,key.toString())
                shootingKey = key
            }
        }
        fun readyToPlay():Boolean{
            return KBD.getKey() == '*'
        }


        fun addInvaders(randomLine: Int, randomNumber: String) {
            if (randomLine == 0) {
                list0 += randomNumber
            } else {
                list1 += randomNumber
            }
            displayWrite(list0, list1, randomLine, 1, hit)
        }

        fun displayStats() {
            LCD.clear()
            cursor.write(0, 0, " Coins:${Statistics.numCoins()}          ")//MUDAR
            cursor.write(1, 0, " Games:${Statistics.numGames()}         ")//MUDAR
        }

        fun man():String {
            cursor.write(0, 0, " On Maintenance ")
            cursor.write(1, 0, "*-Count  #-ShutD  ")

            while (true) {
                val key: Char = KBD.getKey()
                if (key in '0'..'9' ) {
                    println("DHVWAUHDHAWDJHA")
                    return "PLAYING"
                }
                when (key) {
                    '*' -> {
                        displayStats()
                        while (true) {
                            val innerKey: Char = KBD.getKey()
                             if (innerKey in '0'..'9' || innerKey == '*') {
                                return "MANUTENCION"
                            }
                            else if (innerKey == '#'){
                                 cursor.write(0, 0, " Reset Counting?         ") // MUDAR
                                 cursor.write(1, 0, " 5-Yes other-No  ") // MUDAR

                                 while (true) {
                                     val shutdownKey: Char = KBD.getKey()
                                     if (shutdownKey == '5') {
                                         Statistics.resetCounting()
                                         return "MANUTENCION"
                                     } else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') {
                                         return "MANUTENCION"
                                     }
                                 }



                            }
                        }
                    }
                    '#' -> {
                        cursor.write(0, 0, "    Shut Down?         ") // MUDAR
                        cursor.write(1, 0, " 5-Yes other-No   ") // MUDAR
                        while (true) {
                            val shutdownKey: Char = KBD.getKey()
                            if (shutdownKey == '5') {
                                exitProcess(0)
                            } else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') {
                                return "MANUTENCION"
                            }
                        }
                    }
                    else -> {
                        while (true) {
                            val shutdownKey: Char = KBD.getKey()


                            if (shutdownKey in '0'..'9' ) {
                                println("DHVWAUHDHAWDJHA")
                                return "PLAYING"
                            }
                            return "MANUTENCION"

                        }
                    }
                }
            }
        }
        private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

        private fun displayWrite(list0: String, list1: String, line: Int, row: Int, hit: Boolean) {

            val maxLength = 17
            val startingPosition0 = maxLength - (list0.length + 1)
            val startingPosition1 = maxLength - (list1.length + 1)


            if (hit) { //Se um 'hit' ocorrer, limpa apenas a posição do hit e reexibe a linha
                if (line == 0) {
                    LCD.clear()//XXXXXXXXX
                    cursor.displayBars()//XXXXXXXXX
                    cursor.write(0,startingPosition0,list0)
                    cursor.write(1,startingPosition1,list1)
                }
                else {
                    LCD.clear()//XXXXXXXXX
                    cursor.displayBars()//XXXXXXXXX
                    cursor.write(0,startingPosition0,list0)
                    cursor.write(1,startingPosition1,list1)

                }
                cursor.showGun(line, row)
            }

            else{//Atualiza ambas as linhas independentemente da linha atual
                cursor.write(0,startingPosition0,list0)
                cursor.write(1,startingPosition1,list1)
            }
        }

        fun gameOver():String { //Função para exibir a mensagem de fim de jogo
            LCD.clear() //Limpa o LCD
            cursor.write(0,0,"*** GAME OVER **")
            cursor.write(1,0,"Score: ${score*10}            ")
            if (score * 10 > bestScore){
                Time.sleep(1500)
                println("YUDWVAUYDVASHVDKJWAVBKDBVAWIBVADSKJVDKJAWVDHVASJHDVJWHA")
                return newScore()
            }
            return "GAMEOVER"
        }

        private fun newScore() :String{
            var count = 0
            LCD.clear()

            while (true)    {
                Time.sleep(500)
                val key = KBD.getKey()

                if (key == '2') count++

                if (key == '8'){
                    if (letter[count] != 'A'){count--}

                }

                if (key == '5' ) {
                    return "INITIAL"
                }

                cursor.write(0, 0, " Name:${letter[count]}")
                cursor.write(1,0," Score: ${score*10}            ")


            }


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
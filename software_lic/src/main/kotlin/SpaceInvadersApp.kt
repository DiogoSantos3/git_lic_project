import isel.leic.utils.Time

object SpaceInvadersApp {
    private var list0: String = ""
    private var list1: String = ""

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

    fun escreve(list0:String,list2: String ,line:Int,row:Int,hit:Boolean){
        displayBars()
        val maxLength = 17
        val startingPosition1 = maxLength - (list0.length + 1)
        val startingPosition2 = maxLength - (list2.length + 1)


        if (line == 0){
            LCD.cursor(line,startingPosition1)
            LCD.write(list0)

            LCD.cursor(line+1,startingPosition2)
            LCD.write(list2)

            if (hit){
                LCD.cursor(line,1)
                LCD.write(' ')
                LCD.clear()
                displayBars()
            }
        }
        else{
            LCD.cursor(line-1,startingPosition1)
            LCD.write(list0)

            LCD.cursor(line,startingPosition2)
            LCD.write(list2)

            if (hit){
                LCD.cursor(line,1)
                LCD.write(' ')
                LCD.clear()
                displayBars()

            }
        }

    }
    fun playing() {
        displayBars()
        var randomNumber = (0..9).random().toString()
        var randomLine = (0..1).random()

        var line = 0
        val row = 1
        var shootingKey = ' '
        var hit = false

        while (list0.length < 14 || list1.length < 14) {
            randomLine = (0..1).random()
            randomNumber = (0..9).random().toString()

            escreve(list0, list1, line, 1,hit)
            hit=false
            LCD.cursor(line,row)


            println("List0 no inicio do ciclo = ${ list0 }")
            println("List1 no inicio do ciclo = ${ list1 }")
            println("========================================")
            //SHOW INVADERS
            if (randomLine == 0) {
                // Adiciona o número à lista1 e escreve no display
                list0 += randomNumber
                escreve(list0, list1,randomLine,1,hit)


            } else {
                // Adiciona o número à lista2 e escreve no display
                list1 += randomNumber
                escreve(list0, list1,randomLine,1,hit)
            }
            println("List0 no inicio do ciclo = ${ list0 }")
            println("List1 no inicio do ciclo = ${ list1 }")
            println("========================================")

            LCD.cursor(line,row)

            val key = KBD.waitKey(700)

            when(key){
                '*' -> {
                    if(line == 0){
                        line = 1
                        LCD.cursor(line,row)
                    }
                    else{
                        line = 0
                        LCD.cursor(line,row)
                    }
                }

                '#' -> {
                    if(line == 0){
                        if(shootingKey == list0[0]) {
                            list0 = list0.substring(1)
                            println("----------------------------------------")
                            println("List0 no quando atingido = ${ list0 }")
                            println("----------------------------------------")
                            LCD.cursor(line, row)
                            hit = true
                        }
                    }
                    else{
                        if(shootingKey == list1[0]) {
                            list1 = list1.substring(1)
                            println("----------------------------------------")
                            println("List1 no quando atingido = ${ list0 }")
                            println("----------------------------------------")
                            LCD.cursor(line,row)
                            hit = true
                        }

                    }
                }


                else ->{

                    if(line == 0 && key != KBD.NONE ){
                        LCD.cursor(line,row)
                        LCD.write(key)
                        shootingKey = key
                    }
                    else{
                        if (key != KBD.NONE){LCD.cursor(line,row)
                            LCD.write(key)
                            shootingKey = key
                        }
                    }

                }
            }


            Time.sleep(500)



        }
        gameOver(0)


    }
}




fun main() {
    SpaceInvadersApp.init()
    while (true){
        if (KBD.getKey() == '*'){
            LCD.clear()
            SpaceInvadersApp.playing()
            break}

    }

}

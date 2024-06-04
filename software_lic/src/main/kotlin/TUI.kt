import isel.leic.utils.Time
import java.io.File

object TUI {
    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP
    var COIN_MASK = 0x40
    private const val NAME_FILE = "statistics.txt"
    private val text = File(NAME_FILE).readLines()
    private  var num_of_players = text.count()
    private val statistics = mutableListOf<Pair<String, Int>>()//Lista de pares (nome, pontuação)
    enum class Column { UP, DOWN }
     var coin: Int = 0
    var lastState : Boolean = false

    public class Cursor(val line: Int = 0,val row: Int = 1){
        fun write(line: Int,row: Int,word:String){
            LCD.cursor(line,row)
            LCD.write(word) // Escreve a mensagem inicial na primeira linha do
        }

    }


    fun isCoin():Boolean{

        if(HAL.isBit(COIN_MASK)){
            if (!lastState){
                coin += 1
                println("${coin}")
                HAL.setBits(COIN_MASK)
            }
            HAL.clrBits(COIN_MASK)

            return true
        }

        return false
    }

    fun displayStatistics() {

        statistics.clear()

        for (entry in text) {
            val parts = entry.split(";")
            if (parts.size != 2) continue

            val name = parts[0]
            val score = parts[1].toInt()
            statistics.add(Pair(name, score))
        }
        statistics.sortByDescending { it.second }// Ordenar score (second) em ordem decrescente

        var position = 1

        for ((name, score) in statistics) {// Exibir cada par (nome, pontuação)

            if (KBD.getKey() == '*') {
                LCD.clear()
                SpaceInvadersApp.playing()
                return
            }
            LCD.cursor(1, 0)
            LCD.write("$position-$name    $score     ")
            if (num_of_players == position){position = 0}
            position++
            Time.sleep(1000)
        }
    }

    fun init(){
        LCD.init()
        KBD.init()
    }

    fun showGun(line:Int,row:Int){
        LCD.cursor(line,row)
        LCD.write(">")
    }

    val cursor = Cursor()

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

                cursor.write(0,startingPosition0,list0)
                //LCD.cursor(0, startingPosition0)
                //LCD.write(list0)
                cursor.write(1,startingPosition1,list1)
                //LCD.cursor(1, startingPosition1)
                //LCD.write(list1)

            } else {

                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                LCD.clear()
                displayBars()
                //XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
                cursor.write(0,startingPosition0,list0)
                //LCD.cursor(0, startingPosition0)
                //LCD.write(list0)
                cursor.write(1,startingPosition1,list1)
                //LCD.cursor(1, startingPosition1)
                //LCD.write(list1)
            }
            showGun(line, row)
        }

        else{// Atualiza ambas as linhas independentemente da linha atual
            LCD.cursor(0, startingPosition0)
            LCD.write(list0)
            LCD.cursor(1, startingPosition1)
            LCD.write(list1)}
    }
    fun gameOver(score: Int) { // Função para exibir a mensagem de fim de jogo
        LCD.clear() // Limpa o LCD
        LCD.cursor(0, 0)
        LCD.write("*** GAME OVER **") // Mensagem de fim de jogo na primeira linha
        LCD.cursor(1, 0)
        LCD.write("Score: $score ") // Exibe a pontuação na segunda linha
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

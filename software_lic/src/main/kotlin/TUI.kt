import isel.leic.utils.Time
import java.io.File
import kotlin.system.exitProcess

object TUI {

    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP
    private const val NAME_FILE = "SIG_scores.txt"
    private val text = File(NAME_FILE).readLines()

    enum class Column { UP, DOWN }
    var cursor = Cursor()
    var NONE = ' '



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
    fun getKey():Char{return KBD.getKey()}

    fun clear(){return LCD.clear()}

    fun displayWrite(list0: String, list1: String, line: Int, row: Int, hit: Boolean) {

        val maxLength = 17
        val startingPosition0 = maxLength - (list0.length + 1)
        val startingPosition1 = maxLength - (list1.length + 1)


        if (hit) { //Se um 'hit' ocorrer, limpa apenas a posição do hit e reexibe a linha
            if (line == 0) {
                TUI.clear()//XXXXXXXXX
                cursor.displayBars()//XXXXXXXXX
                cursor.write(0,startingPosition0,list0)
                cursor.write(1,startingPosition1,list1)
            }
            else {
                TUI.clear()//XXXXXXXXX
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

    fun newScore(): String {
        var count = 0

        val name = CharArray(10) { ' ' } // Inicializando com espaços ou tamanho apropriado
        var row = 0

        // Inicializando a primeira letra como 'A'
        name[row] = 'A'
        var nome = name.joinToString("")

        cursor = Cursor(0, row)
        cursor.write(0, 0, " Name:${nome}") // Atualização inicial do display
        cursor.write(1, 0, " Score: ${SpaceInvadersApp.score}            ")

        while (true) {
            Time.sleep(100)
            val key = TUI.getKey()

            nome = name.joinToString("") // Atualiza o nome com base no array `name`
            cursor.write(0, 0, " Name:${nome}                ") // Atualização contínua do display

            when (key) {
                '2' -> {
                    if (count < letter.size - 1) {
                        count++
                        name[row] = letter[count]
                    }
                }
                '8' -> {
                    if (count > 0) {
                        count--
                        name[row] = letter[count]
                    }
                }
                '4' -> {
                    if (row > 0) {
                        row--
                        count = letter.indexOf(name[row]) // Atualiza `count` com base na letra atual
                        cursor = Cursor(0, row)
                    }
                }
                '6' -> {
                    if (row < name.size - 1) {
                        row++
                        if (name[row] == ' ') { // Inicializa nova posição com 'A' se vazia
                            name[row] = 'A'
                        }
                        count = letter.indexOf(name[row]) // Atualiza `count` com base na letra atual
                        cursor = Cursor(0, row)
                    }
                }
                '5' -> {
                    return nome
                }
            }

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
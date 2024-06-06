import isel.leic.utils.Time
import java.io.File

object TUI {
    var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    private var nColUP = 0
    private var nColDOWN = 0
    private var actualColumn = Column.UP
    private var COIN_MASK = 0x40
    private const val NAME_FILE = "statistics.txt"
    private val text = File(NAME_FILE).readLines()
    private  var num_of_players = text.count()
    private val statistics = mutableListOf<Pair<String, Int>>()//Lista de pares (nome, pontuação)
    enum class Column { UP, DOWN }
    private var coin: Int = 0
    private var lastState : Boolean = false
    private var cursor = Cursor()
    private var shootingKey = ' ' // Inicializa a tecla de tiro
    var score: Int = 0
    private var hit = false
    class Cursor(val line: Int = 0,val row: Int = 1){
        fun write(line: Int,row: Int,word:String){
            LCD.cursor(line,row)
            LCD.write(word) // Escreve a mensagem inicial na primeira linha do
        }
        fun showGun(line:Int,row:Int){
            cursor.write(line,row,">")
        }

    }
    private fun splitStatistics(){

        statistics.clear() //Limpa a lista de estatísticas antes de começar a preencher novamente

        for (entry in text) { //Processa cada entrada de texto para extrair nome e pontuação
            val parts = entry.split(";") // Divide a string
            val name = parts[0]
            val score = parts[1].toInt()
            statistics.add(Pair(name, score)) //Add Pair(name, score) à lista (statistics)
        }

        statistics.sortByDescending { it.second }//Ordena a lista por ordem decrescente
    }
    fun isCoin():Boolean{

        if(HAL.isBit(COIN_MASK)){
            if (!lastState){
                coin += 2
                println("${coin}")
                HAL.setBits(COIN_MASK)
            }
            HAL.clrBits(COIN_MASK)
            if (coin<=9){cursor.write(1,0," Game X  X X  ${coin}$    ")}
            else{cursor.write(1,0," Game X  X X ${coin}$    ")}

            return true

        }

        return false
    }
    fun displayStatistics(readyToPlay:Boolean):Boolean {

        if(!readyToPlay){

            splitStatistics()

            var position = 1 //Variável para rastrear a posição na lista

            for ((name, score) in statistics) { //Exibir cada Pair(name, score)

                isCoin()

                if (readyToPlay()) {//Verifica se queremos iniciar o jogo
                    LCD.clear() // Limpa o display LCD
                    SpaceInvadersApp.playing() // Inicia o jogo
                    return true// Sai da função
                }


                cursor.write(1, 0, "$position-$name    $score     ") //Escreve a pontuação


                if (num_of_players == position) {//Verifica ja percorreu a a lista toda e reseta a position
                    position = 0
                }
                position++
                Time.sleep(700)

                // Loop para verificação de moedas durante a espera
                for (i in 1..10) {
                    isCoin()
                    Time.sleep(200)// Espera 200 ms antes de próxima verificação
                }
            }
        }
        return false
    }

    private fun newScore(){
        LCD.clear()
        cursor.write(0,0,"IUDWIUAHDIUWAHUDI")
        cursor.write(1,0,"IUDWIUAHDIUWAHUDI")

    }
    fun init(){
        LCD.init()
        KBD.init()
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
    fun addInvaders(randomLine: Int, randomNumber: String) {
        if (randomLine == 0) {
            list0 += randomNumber
        } else {
            list1 += randomNumber
        }
        displayWrite(list0, list1, randomLine, 1, hit)
    }
    private fun changeLine() {

        var linee = cursor.line
        cursor.write(linee,cursor.row," ")
        linee = if (cursor.line == 0) 1 else 0
        cursor.showGun(linee, cursor.row)
        cursor = Cursor(linee,cursor.row)
    }

    fun handleKeyPress() {
        when (val key : Char = KBD.getKey()) {
            '*' -> changeLine()
            '#' -> checkHit()
            else -> displayKey(key)
        }
    }
    private fun displayKey(key: Char) {
        if (key != KBD.NONE) {
            cursor.write(cursor.line, 0,key.toString())
            shootingKey = key
        }
    }
    private fun displayWrite(list0: String, list1: String, line: Int, row: Int, hit: Boolean) {

        val maxLength = 17
        val startingPosition0 = maxLength - (list0.length + 1)
        val startingPosition1 = maxLength - (list1.length + 1)

        // Se um 'hit' ocorrer, limpa apenas a posição do hit e reexibe a linha
        if (hit) {
            if (line == 0) {

                LCD.clear()//XXXXXXXXX
                displayBars()//XXXXXXXXX

                cursor.write(0,startingPosition0,list0)

                cursor.write(1,startingPosition1,list1)


            } else {

                LCD.clear()//XXXXXXXXX
                displayBars()//XXXXXXXXX

                cursor.write(0,startingPosition0,list0)

                cursor.write(1,startingPosition1,list1)

            }
            cursor.showGun(line, row)
        }

        else{// Atualiza ambas as linhas independentemente da linha atual
            cursor.write(0,startingPosition0,list0)
            cursor.write(1,startingPosition1,list1)

            }
    }

    fun readyToPlay():Boolean{
        return KBD.getKey() == '*' && coin >= 2
    }


    fun gameOver(score: Int,newScore:Boolean) { // Função para exibir a mensagem de fim de jogo

        if (newScore){ newScore()}
        else{
            LCD.clear() // Limpa o LCD
            cursor.write(0,0,"*** GAME OVER **")
            cursor.write(1,0,"Score: $score            ")}
    }

    fun displayBars() {
        cursor.write(0,0,"]")
        cursor.write(1,0,"]")

    }

    fun initialDisplay(){
        cursor.write(0,1,"Space Invaders ")//SAIUBDGIWUBA
        cursor.write(1,0," Game X  X X  ${coin}$    ")//SAIUBDGIWUBA


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
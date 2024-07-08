import isel.leic.utils.Time
import java.io.File
import kotlin.system.exitProcess

object TUI { // Define um objeto chamado TUI

    private var nColUP = 0 // Variável para contar o número de colunas na linha superior
    private var nColDOWN = 0 // Variável para contar o número de colunas na linha inferior
    private var actualColumn = Column.UP // Inicializa a coluna atual como a linha superior
    private const val NAME_FILE = "SIG_scores.txt" // Constante para o nome do arquivo de pontuações
    private val text = File(NAME_FILE).readLines() // Lê as linhas do arquivo de pontuações e armazena em 'text'

    enum class Column { UP, DOWN } // Enumeração para representar as colunas (superior e inferior)
    var cursor = Cursor() // Inicializa o cursor
    var NONE = ' ' // Constante para representar nenhuma tecla pressionada

    var bestScore: Int = 0 // Variável para armazenar a melhor pontuação
    private var letter = listOf('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z') // Lista de letras

    // Classe Cursor para manipulação do cursor no display LCD
    class Cursor(val line: Int = 0, val row: Int = 1) {
        fun write(line: Int, row: Int, word: String) {
            LCD.cursor(line, row)
            LCD.write(word) // Escreve a mensagem inicial na posição especificada
        }
        fun showGun(line: Int, row: Int) {
            LCD.spaceShip(line, row) // Mostra a nave espacial na posição especificada
        }
        fun displayBars() {
            cursor.write(0, 0, "]") // Exibe barras nas linhas 0 e 1
            cursor.write(1, 0, "]")
        }
        fun special_characters() {
            LCD.invader(1, 9) // Desenha um invasor na posição (1, 9)
            LCD.invader(1, 11) // Desenha um invasor na posição (1, 11)
            LCD.spaceShip(1, 6) // Desenha uma nave espacial na posição (1, 6)
        }
    }

    fun init() {
        LCD.init() // Inicializa o LCD
        KBD.init() // Inicializa o teclado
    }

    fun getKey(): Char {
        return KBD.getKey() // Retorna a tecla pressionada
    }

    fun clear() {
        return LCD.clear() // Limpa o display LCD
    }

    fun displayWrite(list0: String, list1: String, line: Int, row: Int, hit: Boolean) {
        val maxLength = 17
        val startingPosition0 = maxLength - (list0.length + 1)
        val startingPosition1 = maxLength - (list1.length + 1)

        if (hit) { // Se um 'hit' ocorrer, limpa apenas a posição do hit e reexibe a linha
            TUI.clear()
            cursor.displayBars()
            cursor.write(0, startingPosition0, list0)
            cursor.write(1, startingPosition1, list1)
            cursor.showGun(line, row) // Mostra a nave espacial na posição especificada
        } else { // Atualiza ambas as linhas independentemente da linha atual
            cursor.write(0, startingPosition0, list0)
            cursor.write(1, startingPosition1, list1)
        }
    }

    fun newScore(): String {
        var count = 0
        val name = CharArray(8) { ' ' } // Inicializa com espaços ou tamanho apropriado
        var row = 0
        name[row] = 'A' // Inicializa a primeira letra como 'A'
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
                    return nome // Retorna o nome quando a tecla '5' for pressionada
                }
            }
        }
    }

    fun writeKey(time: Long) {
        var temp = KBD.waitKey(time) // Obtém a tecla pressionada
        while (temp != KBD.NONE) { // Enquanto a tecla não for nenhuma...
            when (temp) {
                '#' -> { // Se a tecla pressionada for '#', limpa o display e reseta as colunas
                    LCD.clear()
                    nColUP = 0 // Coluna 0 fica com 0 números
                    nColDOWN = 0 // Coluna 1 fica com 0 números
                }
                '*' -> {
                    if (actualColumn == Column.UP) { // Se a coluna atual for a linha superior, muda para a inferior
                        LCD.cursor(1, nColDOWN)
                        actualColumn = Column.DOWN
                    } else { // Se a coluna atual for a linha inferior, muda para a superior
                        LCD.cursor(0, nColUP)
                        actualColumn = Column.UP
                    }
                }
                else -> { // Senão, escreve no LCD a tecla pressionada e incrementa o contador da coluna
                    LCD.write(temp)
                    if (actualColumn == Column.UP) {
                        nColUP++
                    } else {
                        nColDOWN++
                    }
                }
            }
            temp = KBD.waitKey(time) // Obtém a próxima tecla pressionada
        }
    }
}

fun main() {
    HAL.init() // Inicializa o HAL
    KBD.init() // Inicializa o teclado
    LCD.init() // Inicializa o LCD
    SerialEmitter.init() // Inicializa o SerialEmitter

    LCD.clear() // Limpa o LCD
    LCD.cursor(0, 0) // Posiciona o cursor no início
    LCD.write("test") // Escreve "test" no LCD
    TUI.writeKey(99999999999999999) // Chama a função writeKey com um tempo de espera muito longo
}

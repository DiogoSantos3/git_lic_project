import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter
object FilesAccess {
    private const val NAME_STATS_FILE = "scores.txt"
    private const val X = "x.txt"
    private var num_of_players = NAME_STATS_FILE.count()
    fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }

    fun numOfPlayers():Int{
        return NAME_STATS_FILE.count()
    }

    fun createWriter(fileName: String): PrintWriter {
        return PrintWriter(fileName)
    }

    fun escrever(coin: String, jogo: String) {
        val pw = createWriter("x.txt")
        pw.println(coin.toString())
        pw.println(jogo.toString())
        pw.close()
    }

    fun ler(fileName: String): List<String> {
        val fr = createReader(fileName)
        return fr.readLines()
    }

    fun resetFile(fileName: String) {
        escrever(0.toString(), 0.toString())
    }
}


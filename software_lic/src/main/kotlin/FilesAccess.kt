import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter
import java.io.FileWriter
object FilesAccess {
    private const val NAME_STATS_FILE = "SIG_scores.txt"
    private const val X = "statistics.txt"
    private var num_of_players = NAME_STATS_FILE.count()
    fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }

    fun numOfPlayers():Int{
        return NAME_STATS_FILE.count()
    }


    fun writeStatistics(coin: String, jogo: String) {
        val pw = createWriter(X)
        pw.println(coin)
        pw.println(jogo)
        pw.close()
    }


    fun createWriter(fileName: String, append: Boolean = false): PrintWriter {
        return PrintWriter(FileWriter(fileName, append))
    }


    fun ler(fileName: String): List<String> {
        val fr = createReader(fileName)
        return fr.readLines()
    }



}
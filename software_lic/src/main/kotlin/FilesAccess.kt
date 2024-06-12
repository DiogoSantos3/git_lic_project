import java.io.File
import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter
object FilesAccess{

    private const val NAME_STATS_FILE = "statistics.txt"
    private const val X = "x.txt"
    fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName))
    }

    fun createWriter(fileName: String): PrintWriter {
        return PrintWriter(fileName)
    }


    fun escrever(fileName: String,coin:Int,jogo:Int){
        val pw = createWriter("hashTags.txt")
        pw.println(coin)


    }
}
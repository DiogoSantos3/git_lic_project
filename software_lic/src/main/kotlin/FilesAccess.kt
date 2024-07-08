import java.io.BufferedReader
import java.io.FileReader
import java.io.PrintWriter
import java.io.FileWriter

// Define um objeto chamado FilesAccess
object FilesAccess {
    // Constante que armazena o nome do arquivo de pontuações
    private const val NAME_STATS_FILE = "SIG_scores.txt"
    // Constante que armazena o nome do arquivo de estatísticas
    private const val X = "statistics.txt"
    // Variável que armazena o número de jogadores baseado na contagem de caracteres do nome do arquivo de pontuações
    private var num_of_players = NAME_STATS_FILE.count()

    // Função para criar um BufferedReader para ler um arquivo especificado
    fun createReader(fileName: String): BufferedReader {
        return BufferedReader(FileReader(fileName)) // Retorna um BufferedReader que lê o arquivo especificado
    }

    // Função para obter o número de jogadores
    fun numOfPlayers(): Int {
        return NAME_STATS_FILE.count() // Retorna a contagem de caracteres do nome do arquivo de pontuações
    }

    // Função para escrever estatísticas em um arquivo
    fun writeStatistics(coin: String, jogo: String) {
        val pw = createWriter(X) // Cria um PrintWriter para escrever no arquivo de estatísticas
        pw.println(coin) // Escreve a string `coin` no arquivo
        pw.println(jogo) // Escreve a string `jogo` no arquivo
        pw.close() // Fecha o PrintWriter
    }

    // Função para criar um PrintWriter para escrever em um arquivo especificado
    fun createWriter(fileName: String, append: Boolean = false): PrintWriter {
        return PrintWriter(FileWriter(fileName, append)) // Retorna um PrintWriter que escreve no arquivo especificado
    }

    // Função para ler todas as linhas de um arquivo e retorná-las como uma lista de strings
    fun ler(fileName: String): List<String> {
        val fr = createReader(fileName) // Cria um BufferedReader para ler o arquivo especificado
        return fr.readLines() // Lê todas as linhas do arquivo e retorna como uma lista de strings
    }
}

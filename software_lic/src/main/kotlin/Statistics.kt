import java.io.File

object Statistics {

    private const val NAME_FILE = "statistics.txt"
    private val text = File(NAME_FILE).readLines()
    val num_of_players = text.count()
    private val statistics = mutableListOf<Pair<String, Int>>() // Lista de pares (nome, pontuação)
    private var bestScore: Int = 0

    fun splitStatistics(): List<Pair<String, Int>> {
        statistics.clear() // Limpa a lista de estatísticas antes de começar a preencher novamente

        for (entry in text) { // Processa cada entrada de texto para extrair nome e pontuação
            val parts = entry.split(";") // Divide a string
            val name = parts[0]
            val score = parts[1].toInt()
            statistics.add(Pair(name, score)) // Add Pair(name, score) à lista (statistics)
        }

        statistics.sortByDescending { it.second } // Ordena a lista por ordem decrescente
        bestScore = statistics.first().second

        return statistics
    }

}
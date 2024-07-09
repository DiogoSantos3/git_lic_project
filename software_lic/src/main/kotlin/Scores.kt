object Scores { // Define um objeto chamado Scores

    private val text = FilesAccess.ler("SIG_scores.txt") // Lê o conteúdo do arquivo "SIG_scores.txt" e guarda em 'text'
    var num_of_players = text.count() // Conta o número de caracteres em 'text' e guarda em 'num_of_players'

    private const val SCORES_FILE = "SIG_scores.txt" // Declara uma constante privada para o nome do arquivo de pontuações

    // Função que separa as pontuações e retorna uma lista de pares (nome, pontuação)
    fun splitScores(): List<Pair<String, String>> {
        val scores = readScores() // Lê as pontuações do arquivo

        return scores.mapNotNull { score -> // Mapeia cada pontuação
            val parts = score.split(";") // Separa cada pontuação usando ";" como delimitador
            if (parts.size == 2) { // Verifica se há exatamente duas partes
                parts[0] to parts[1] // Retorna um par (nome, pontuação)
            } else {
                null // Retorna null se o formato estiver incorreto
            }
        }.sortedByDescending { it.second.toIntOrNull() ?: 0 } // Ordena a lista por pontuação em ordem decrescente

    }

    // Função que lê as pontuações do arquivo
    fun readScores(): List<String> {
        return FilesAccess.ler(SCORES_FILE) // Usa FilesAccess para ler o arquivo de pontuações
    }

    // Função que retorna a maior pontuação
    fun getTopScore(): String? {
        return splitScores().firstOrNull()?.second // Retorna a maior pontuação ou null se não houver pontuações
    }

    // Função que retorna a última pontuação
    fun getLastScore(): String? {
        return splitScores().lastOrNull()?.second // Retorna a última pontuação ou null se não houver pontuações
    }

    // Função que escreve o nome e a pontuação de um jogador no arquivo
    fun writePlayers(name: String, score: String) {
        val pw = FilesAccess.createWriter(SCORES_FILE, append = true) // Cria um PrintWriter para escrever no arquivo, em modo de append
        println() // Imprime uma linha em branco no console
        pw.println("${name.trim()};$score") // Escreve o nome e a pontuação no arquivo, separados por ";"
        pw.close() // Fecha o PrintWriter
    }

    var names = splitScores() // Inicializa 'names' com a lista de pontuações separadas

}

fun main() {
    println(Scores.getTopScore()) // Imprime a maior pontuação na consola
}

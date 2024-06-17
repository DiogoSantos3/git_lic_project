object Scores {
    private val text = FilesAccess.ler("scores.txt")
    var num_of_players = text.count()
    private const val NAME_FILE = "scores.txt"
    private val scores = mutableListOf<Pair<String, Int>>() // Lista de pares (nome, pontuação)
    private var bestScore: Int = 0

    fun splitScores(): List<Pair<String, Int>> {
        scores.clear() // Limpa a lista de estatísticas antes de começar a preencher novamente
        val text = FilesAccess.ler(NAME_FILE)

        for (entry in text) { // Processa cada entrada de texto para extrair nome e pontuação
            val parts = entry.split(";") // Divide a string
            val name = parts[0]
            val score = parts[1].toInt()
            scores.add(Pair(name, score)) // Add Pair(name, score) à lista (statistics)
        }

        scores.sortByDescending { it.second } // Ordena a lista por ordem decrescente
        bestScore = scores.first().second

        return scores
    }


}
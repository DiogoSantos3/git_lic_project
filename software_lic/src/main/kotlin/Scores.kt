object Scores {
    private val text = FilesAccess.ler("scores.txt")
    var num_of_players = text.count()
    private const val NAME_FILE = "scores.txt"
    private val scores = mutableListOf<Pair<String, Int>>() // Lista de pares (nome, pontuação)
    private var bestScore: Int = 0
    private const val SCORES_FILE = "scores.txt"
    fun splitScores(): List<Pair<String, String>> {
        val scores = readScores()

        return scores.mapNotNull { score ->
            val parts = score.split(";")
            if (parts.size == 2) {
                parts[0] to parts[1]
            } else {
                null
            }
        }.sortedByDescending { it.second.toIntOrNull() ?: 0 }
    }
    fun readScores(): List<String> {
        return FilesAccess.ler(SCORES_FILE)
    }
    fun getTopScore(): Pair<String, String>? {
        return splitScores().firstOrNull()
    }
    fun writePlayers(name: String, score: String) {
        val pw = FilesAccess.createWriter(SCORES_FILE, append = true)
        println()
        pw.println("${name.trim()};$score")
        pw.close()
    }

    var names = splitScores()




}

fun main(){
    println(Scores.splitScores())
}
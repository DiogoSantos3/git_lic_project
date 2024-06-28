object Scores {
    private val text = FilesAccess.ler("SIG_scores.txt")
    var num_of_players = text.count()

    private const val SCORES_FILE = "SIG_scores.txt"


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
    fun getTopScore(): String {
        return splitScores().first().second
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
    println(Scores.getTopScore())
}
object Statistics {
    private const val NAME_STATS_FILE = "statistics.txt"
    fun numCoins(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE)
        return if (lines.isNotEmpty()) lines[0] else "0"
    }

    fun numGames(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE)
        return if (lines.size > 1) lines[1] else "0"
    }

    fun addCoins(ACK:Boolean) {
        val numGames = numGames().toInt()
        val numCoins = numCoins().toInt()
        if (ACK){FilesAccess.writeStatistics((numCoins + 2).toString(), numGames.toString())}
        else{FilesAccess.writeStatistics((numCoins -2 ).toString(), numGames.toString())}

    }



    fun addGames() {
        val numCoins = numCoins().toInt()
        val numGames = numGames().toInt()
        FilesAccess.writeStatistics(numCoins.toString(), (numGames + 1).toString())
    }

    fun resetCounting() {
        FilesAccess.writeStatistics(0.toString(), 0.toString())
    }
}

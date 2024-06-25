object Statistics {
    private const val NAME_STATS_FILE = "x.txt"
    fun numCoins(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE)
        return if (lines.isNotEmpty()) lines[0] else "0"
    }

    fun numGames(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE)
        return if (lines.size > 1) lines[1] else "0"
    }

    fun addCoins(numCoin: Int) {
        val numGames = numGames().toInt()
        FilesAccess.writeStatistics((numCoin + 2).toString(), numGames.toString())
    }

    fun addGames(numGames: Int) {
        val numCoins = numCoins().toInt()
        FilesAccess.writeStatistics(numCoins.toString(), numGames.toString())
    }

    fun resetCounting() {
        FilesAccess.writeStatistics(0.toString(), 0.toString())
    }
}

object Statistics {

    fun numCoins(): String {
        val lines = FilesAccess.ler("x.txt")
        return if (lines.isNotEmpty()) lines[0] else "0"
    }

    fun numGames(): String {
        val lines = FilesAccess.ler("x.txt")
        return if (lines.size > 1) lines[1] else "0"
    }

    fun addCoins(numCoin: Int) {
        val numGames = numGames().toInt()
        FilesAccess.escrever((numCoin + 2).toString(), numGames.toString())
    }

    fun addGames(numGames: Int) {
        val numCoins = numCoins().toInt()
        FilesAccess.escrever(numCoins.toString(), numGames.toString())
    }

    fun resetCounting() {
        FilesAccess.escrever(0.toString(), 0.toString())
    }
}

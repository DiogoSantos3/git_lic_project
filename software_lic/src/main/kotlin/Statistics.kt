object Statistics { // Define um objeto chamado Statistics

    private const val NAME_STATS_FILE = "statistics.txt" // Declara uma constante privada para o nome do arquivo de estatísticas

    // Função para obter o número de moedas
    fun numCoins(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE) // Lê as linhas do arquivo de estatísticas
        return if (lines.isNotEmpty()) lines[0] else "0" // Retorna a primeira linha se o arquivo não estiver vazio, caso contrário retorna "0"
    }

    // Função para obter o número de jogos
    fun numGames(): String {
        val lines = FilesAccess.ler(NAME_STATS_FILE) // Lê as linhas do arquivo de estatísticas
        return if (lines.size > 1) lines[1] else "0" // Retorna a segunda linha se houver pelo menos duas linhas, caso contrário retorna "0"
    }

    // Função para adicionar ou remover moedas
    fun addCoins(ACK: Boolean) {
        val numGames = numGames().toInt() // Obtém o número de jogos como um inteiro
        val numCoins = numCoins().toInt() // Obtém o número de moedas como um inteiro
        if (ACK) {
            // Se ACK for verdadeiro, adiciona 2 moedas
            FilesAccess.writeStatistics((numCoins + 2).toString(), numGames.toString())

        }
            else {
            FilesAccess.writeStatistics((numCoins - 2).toString(), numGames.toString())
            }

    }

    // Função para adicionar um jogo
    fun addGames() {
        val numCoins = numCoins().toInt() // Obtém o número de moedas como um inteiro
        val numGames = numGames().toInt() // Obtém o número de jogos como um inteiro
        FilesAccess.writeStatistics(numCoins.toString(), (numGames + 1).toString()) // Escreve as novas estatísticas no arquivo, incrementando o número de jogos em 1
    }

    // Função para resetar a contagem de moedas e jogos
    fun resetCounting() {
        FilesAccess.writeStatistics(0.toString(), 0.toString()) // Reseta as estatísticas no arquivo, definindo moedas e jogos para 0
    }
}

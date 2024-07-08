object CoinAccepter {
    // Variável para armazenar o último estado do bit da moeda
    private var lastState: Boolean = true
    // Máscara de bits para verificar o estado da moeda
    private var COIN_MASK = 0x40
    // Variável para contar o número de moedas
    var coin = 0

    // Função para verificar se uma moeda foi inserida
    fun isCoin(): Boolean {
        // Verifica se o bit da moeda não está definido e o último estado era verdadeiro
        if (!HAL.isBit(COIN_MASK) && lastState) {
            // Atualiza o último estado para falso
            lastState = false
        }
        // Verifica se o bit da moeda está definido e o último estado era falso
        if (HAL.isBit(COIN_MASK) && !lastState) {
            // Atualiza o último estado para verdadeiro
            lastState = true
            // Retorna verdadeiro indicando que uma moeda foi detectada
            return true
        }
        // Retorna falso se nenhuma moeda foi detectada
        return false
    }
}

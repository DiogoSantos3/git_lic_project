import isel.leic.utils.Time

object ScoreDisplay { // Controla o mostrador de pontuação.

    // Inicializa a classe, estabelecendo os valores iniciais.
    fun init() {
        SerialEmitter.init() // Inicializa o emissor serial.
        off(false) // Liga o display de pontuação.
    }

    // Envia comando para atualizar o valor do mostrador de pontuação.
    fun setScore(value: Int) {
        // Formata o valor com 5 dígitos, preenchendo com zeros à esquerda.
        val formattedValue = value.toString().padStart(5, '0')
        var num = 5

        // Itera sobre cada caractere do valor formatado.
        for (i in formattedValue) {
            // Converte o caractere em um dígito inteiro.
            val bin = i.digitToInt()
            // Calcula o valor binário a ser enviado.
            val vall = bin.shl(3) + num

            // Envia o valor binário para o display de pontuação.
            SerialEmitter.send(SerialEmitter.Destination.SCORE, vall, 7)
            num-- // Decrementa o número para o próximo dígito.
        }
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
    }

    // Envia comando para desativar/ativar a visualização do mostrador de pontuação.
    fun off(value: Boolean) {
        // Define o comando para ligar ou desligar o display.
        val data = if (value) 0b0001111 else 0b0000111
        // Envia o comando para o display de pontuação.
        SerialEmitter.send(addr = SerialEmitter.Destination.SCORE, data, 7)
    }
}

fun main() {
    ScoreDisplay.init() // Inicializa o display de pontuação.

    // Loop que conta de 0 a 100.
    for (x in 0..100) {
        Time.sleep(1000) // Espera 1 segundo entre cada incremento.
        ScoreDisplay.setScore(x) // Atualiza o display com o valor atual de x.
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
    }

    Time.sleep(2000) // Espera 2 segundos antes de desligar o display.
    ScoreDisplay.off(true) // Desliga o display de pontuação.
}
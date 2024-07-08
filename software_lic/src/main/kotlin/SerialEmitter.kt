object SerialEmitter { // Envia tramas para os diferentes módulos Serial Receiver.
    val SSLCD_MASK = 0x01 // Máscara para o sinal do LCD
    val SSCORE_MASK = 0x02 // Máscara para o sinal de pontuação
    val SCLOCK_MASK = 0x10 // Máscara para o sinal de clock
    val SDX_MASK = 0x08 // Máscara para o sinal de dados
    enum class Destination { LCD, SCORE } // Enumeração para os destinos possíveis (LCD e SCORE)

    // Inicia a classe
    fun init() {
        HAL.init() // Inicializa o HAL (Hardware Abstraction Layer)
        HAL.setBits(SSLCD_MASK) // Ativa o sinal do LCD
        HAL.setBits(SSCORE_MASK) // Ativa o sinal de pontuação
        HAL.clrBits(SCLOCK_MASK) // Desativa o sinal de clock
    }

    // Envia uma trama para o SerialReceiver identificando o destino em addr, os bits de dados em
    // ‘data’ e em size o número de bits a enviar.
    fun send(addr: Destination, data: Int, size: Int) {
        val mask = if (addr == Destination.LCD) SSLCD_MASK else SSCORE_MASK // Seleciona a máscara apropriada com base no destino
        var parity = 0 // Inicializa a paridade
        val frame = data // Atribui os dados à variável frame
        var bitMask = 0x01 // Inicializa a máscara de bits

        HAL.clrBits(mask) // Transição descendente do sinal do destino

        for (i in 0 until size) { // Loop para cada bit a ser enviado

            if ((frame and bitMask) != 0) { // Verifica se o bit atual em data é '1'
                parity++ // Incrementa a paridade
                HAL.setBits(SDX_MASK) // Define o bit de dados para '1'
            } else {
                HAL.clrBits(SDX_MASK) // Define o bit de dados para '0'
            }

            HAL.setBits(SCLOCK_MASK) // Dá um clock
            HAL.clrBits(SCLOCK_MASK) // Dá um clock

            bitMask = bitMask shl 1 // Faz shift left para verificar o próximo bit
        }

        if (parity % 2 != 0) { // Verifica se a quantidade de '1's é ímpar
            HAL.setBits(SDX_MASK) // Define o bit de paridade para '1'
        } else {
            HAL.clrBits(SDX_MASK) // Define o bit de paridade para '0'
        }

        HAL.setBits(SCLOCK_MASK) // Dá um clock
        HAL.clrBits(SCLOCK_MASK) // Dá um clock
        HAL.setBits(mask) // Transição ascendente do sinal do destino (transmissão concluída)
    }
}

fun main() {
    HAL.init() // Inicializa o HAL
    SerialEmitter.init() // Inicializa o SerialEmitter
    SerialEmitter.send(SerialEmitter.Destination.LCD, 0x155, 9) // Envia uma trama para o LCD com dados 0x155 e tamanho 9 bits
}

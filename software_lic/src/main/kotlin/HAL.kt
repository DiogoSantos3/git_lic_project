import isel.leic.UsbPort

// Define um objeto singleton chamado HAL
object HAL {
    // Variável para armazenar o último valor de saída escrito no UsbPort
    var last_Output = 0

    // Inicializa a classe HAL escrevendo o último valor de saída no UsbPort
    fun init() {
        UsbPort.write(last_Output) // Escreve last_Output no UsbPort
    }

    // Verifica se um bit específico está definido para 1
    fun isBit(mask: Int): Boolean {
        val read = UsbPort.read() // Lê o valor atual do UsbPort
        return read.and(mask) != 0 // Retorna true se o bit correspondente à máscara estiver definido para 1
    }

    // Lê os bits representados pela máscara no UsbPort
    fun readBits(mask: Int): Int {
        val read = UsbPort.read() // Lê o valor atual do UsbPort
        return read.and(mask) // Retorna os bits correspondentes à máscara
    }

    // Define os bits representados pela máscara para 1 (acende os LEDs)
    fun setBits(mask: Int) {
        last_Output = mask.or(last_Output) // Define os bits da máscara no last_Output
        UsbPort.write(last_Output) // Escreve last_Output atualizado no UsbPort
    }

    // Limpa os bits representados pela máscara (define para 0)
    fun clrBits(mask: Int) {
        last_Output = mask.inv().and(last_Output) // Limpa os bits da máscara no last_Output
        UsbPort.write(last_Output) // Escreve last_Output atualizado no UsbPort
    }

    // Escreve os bits representados pela máscara com o valor especificado
    fun writeBits(mask: Int, value: Int) {
        clrBits(mask) // Primeiro, limpa os bits da máscara
        setBits(value.and(mask)) // Em seguida, define os bits com o valor ANDed com a máscara
    }
}


fun main() {
    HAL.init() // Inicializa a classe HAL
    while (true) { // Loop infinito para testar as funções HAL
        //println(HAL.isBit(2)) // Comenta esta linha, que verificaria se o bit 2 está definido
        //println(HAL.readBits(3)) // Comenta esta linha, que leria os bits representados pela máscara 3
        //println(HAL.writeBits(7, 3)) // Comenta esta linha, que escreveria o valor 3 nos bits representados pela máscara 7
        //println(HAL.clrBits(3)) // Comenta esta linha, que limparia os bits representados pela máscara 3
        //println(HAL.setBits(3)) // Comenta esta linha, que definiria os bits representados pela máscara 3
    }
}

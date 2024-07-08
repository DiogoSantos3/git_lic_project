import isel.leic.utils.Time

// Define um objeto  chamado KBD para ler teclas. Métodos retornam ‘0’..’9’,’#’,’*’ ou NONE.
object KBD {

    // Constantes para máscaras de bits e teclas do teclado
    const val KEY = 0X0F        // Mapeia as teclas do teclado (kbd.K[0-3] -> UsbPort.I[0-3])
    const val KACK_MASK = 0X80  // Máscara para o sinal de reconhecimento do teclado (UsbPort.O7 -> kbd.ack)
    const val KVAL_MASK = 0X10  // Máscara para o sinal de valor do teclado (kbd.val -> UsbPort.I4)
    const val NONE = ' '        // Constante para representar nenhuma tecla pressionada
    val keys: CharArray = charArrayOf('1', '4', '7', '*', '2', '5', '8', '0', '3', '6', '9', '#') // Mapeamento das teclas do teclado

    // Função para inicializar a classe KBD
    fun init() {
        HAL.init() // Inicializa o HAL (Hardware Abstraction Layer)
    }

    // Função para retornar imediatamente a tecla pressionada ou NONE se nenhuma tecla estiver pressionada
    fun getKey(): Char {
        // Verifica se alguma tecla está sendo pressionada
        if (HAL.isBit(KVAL_MASK)) {
            val tecla = HAL.readBits(KEY) // Lê o valor da tecla pressionada
            HAL.setBits(KACK_MASK) // Ativa o sinal de reconhecimento do teclado

            // Verifica se não há mais teclas sendo pressionadas
            if (!HAL.isBit(KVAL_MASK)) {
                HAL.clrBits(KACK_MASK) // Desativa o sinal de reconhecimento para iniciar um novo varrimento
                return keys[tecla] // Retorna o caractere correspondente à tecla pressionada
            }
        }
        return NONE // Retorna NONE se nenhuma tecla foi pressionada
    }

    // Função para retornar a tecla pressionada antes do timeout em milissegundos acabar, ou NONE caso contrário
    fun waitKey(timeout: Long): Char {
        val timeFinal = timeout + Time.getTimeInMillis() // Calcula o tempo final adicionando o timeout ao tempo atual em milissegundos
        var key = NONE // Inicializa a variável key como NONE

        // Loop para buscar a tecla pressionada até o timeout expirar
        do {
            key = getKey() // Lê a tecla pressionada
        } while (Time.getTimeInMillis() < timeFinal && key == NONE) // Continua enquanto o tempo atual for menor que o tempo final e nenhuma tecla foi pressionada

        return key // Retorna a tecla pressionada ou NONE se o timeout expirar
    }
}


fun main() {
    KBD.init() // Inicia a classe KBD
    while (true) { // Loop infinito para testar a leitura de teclas
        println(KBD.waitKey(99999999999)) // Imprime a tecla pressionada ou NONE após esperar por uma tecla indefinidamente
    }
}

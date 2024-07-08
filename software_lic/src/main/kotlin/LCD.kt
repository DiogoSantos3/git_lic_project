import isel.leic.utils.Time

// Define um objeto chamado LCD para escrever no LCD usando a interface a 4 bits
object LCD {

    const val SERIAL_INTERFACE = true // Constante para definir o uso da interface serial
    const val E_MASK = 0x20           // Máscara de bits para o sinal de enable (UsbPort.O5 -> lcd.e)
    const val RS_MASK = 0x10          // Máscara de bits para o sinal de registro (UsbPort.O4 -> lcd.rs)
    const val CLK_REG_MASK = 0x40     // Máscara de bits para o clock do registrador (UsbPort.O6 -> regLow.clk, regHigh.clk)
    const val DATA_MASK = 0x0F        // Máscara de bits para os dados (UsbPort.O[0-3] -> regLow.in[0-3])

    // Função para escrever um byte de comandos/dados no LCD em paralelo
    fun writeByteParallel(rs: Boolean, data: Int) {
        val sr = if (rs) 1 else 0 // Define sr como 1 se rs for true, caso contrário define como 0

        if (sr == 1) {
            HAL.setBits(RS_MASK) // Define o bit de RS para mensagem de dados
        } else {
            HAL.clrBits(RS_MASK) // Limpa o bit de RS para mensagem de controle
        }

        HAL.setBits(E_MASK) // Ativa o enable

        val high = data.shr(4) // Shift para obter os 4 bits mais significativos

        HAL.writeBits(DATA_MASK, high) // Escreve os 4 bits mais significativos no registrador baixo
        HAL.setBits(CLK_REG_MASK) // Ativa o clock
        HAL.clrBits(CLK_REG_MASK) // Desativa o clock

        HAL.writeBits(DATA_MASK, data) // Escreve os 4 bits menos significativos
        HAL.setBits(CLK_REG_MASK) // Ativa o clock
        HAL.clrBits(CLK_REG_MASK) // Desativa o clock

        HAL.clrBits(E_MASK) // Desativa o enable
    }

    // Função para escrever um byte de comandos/dados no LCD em série
    fun writeByteSerial(rs: Boolean, data: Int) {
        val sr = if (rs) 1 else 0 // Define sr como 1 se rs for true, caso contrário define como 0
        SerialEmitter.send(SerialEmitter.Destination.LCD, data.shl(1) + sr, 9) // Envia os dados em série para o LCD
    }

    // Função para escrever um byte de comandos/dados no LCD
    fun writeByte(rs: Boolean, data: Int) {
        if (SERIAL_INTERFACE) {
            writeByteSerial(rs, data) // Escreve o byte usando a interface serial
        } else {
            writeByteParallel(rs, data) // Escreve o byte usando a interface paralela
        }
    }

    // Função para escrever uma mensagem de controle
    fun writeCMD(data: Int) {
        writeByte(false, data) // Chama writeByte com rs como false para mensagem de controle
    }

    // Função para escrever uma mensagem de dados
    fun writeDATA(data: Int) {
        writeByte(true, data) // Chama writeByte com rs como true para mensagem de dados
    }

    // Função para enviar a sequência de inicialização para a comunicação de 4 bits
    fun init() {
        SerialEmitter.init() // Inicializa o emissor serial
        writeCMD(0b00110000)
        Time.sleep(15)
        writeCMD(0b00110000)
        Time.sleep(1)
        writeCMD(0b00110000)
        writeCMD(0b00111000)
        writeCMD(0b00001000)
        writeCMD(0b00000001)
        writeCMD(0b00000110)
        writeCMD(0b00001111)
        clear() // Limpa o display
        cursor(0, 0) // Posiciona o cursor no início
    }

    // Função para escrever um caráter na posição corrente
    fun write(c: Char) {
        writeDATA(c.code) // Escreve o código do caráter como dado
    }

    // Função para escrever uma string na posição corrente
    fun write(text: String) {
        for (element in text) { // Itera sobre cada caráter na string
            write(element) // Escreve o caráter
        }
    }

    // Função para desenhar um invasor no display
    fun invader(line: Int, row: Int) {
        writeCMD(0b01000000) // Define o endereço CGRAM para o início do caractere personalizado
        writeDATA(0b00011111) // Primeira linha do invasor
        writeDATA(0b00011111) // Segunda linha do invasor
        writeDATA(0b00010101) // Terceira linha do invasor
        writeDATA(0b00011111) // Quarta linha do invasor
        writeDATA(0b00011111) // Quinta linha do invasor
        writeDATA(0b00010001) // Sexta linha do invasor
        writeDATA(0b00010001) // Sétima linha do invasor
        writeDATA(0b00000000) // Oitava linha do invasor (vazia)
        cursor(line, row) // Posiciona o cursor na posição especificada
        writeDATA(0) // Escreve o caractere personalizado no display
        cursor(20, 20) // Move o cursor para fora da tela (posição invisível)
    }

    // Função para desenhar uma nave espacial no display
    fun spaceShip(line: Int, row: Int) {
        writeCMD(0b01001000) // Define o endereço CGRAM para o início do caractere personalizado
        writeDATA(0b00011110) // Primeira linha da nave espacial
        writeDATA(0b00011000) // Segunda linha da nave espacial
        writeDATA(0b00011100) // Terceira linha da nave espacial
        writeDATA(0b00011111) // Quarta linha da nave espacial
        writeDATA(0b00011100) // Quinta linha da nave espacial
        writeDATA(0b00011000) // Sexta linha da nave espacial
        writeDATA(0b00011110) // Sétima linha da nave espacial
        writeDATA(0b00000000) // Oitava linha da nave espacial (vazia)
        cursor(line, row) // Posiciona o cursor na posição especificada
        writeDATA(1) // Escreve o caractere personalizado no display
        cursor(20, 20) // Move o cursor para fora da tela (posição invisível)
    }

    // Função para enviar comandos para posicionar o cursor
    fun cursor(line: Int, row: Int) {
        writeCMD((line * 0X40 + row) or 0x80) // Calcula e envia o comando para posicionar o cursor
    }

    // Função para enviar comandos para limpar o display e posicionar o cursor no (0,0)
    fun clear() {
        writeCMD(0b00000001) // Envia o comando para limpar o display
    }
}

// Função main para testar a classe LCD
fun main() {
    LCD.init() // Inicializa a classe LCD
    while (true) { // Loop infinito para testar a escrita no display LCD
        LCD.clear() // Limpa o display
        LCD.write("Hello World") // Escreve "Hello World" no display
        Thread.sleep(2000) // Espera por 2 segundos
        println("OFF?") // Imprime "OFF?" no console
        LCD.writeCMD(0b00001111) // Envia o comando para ligar o display com cursor e piscar
        Thread.sleep(2000) // Espera por 2 segundos
    }
}

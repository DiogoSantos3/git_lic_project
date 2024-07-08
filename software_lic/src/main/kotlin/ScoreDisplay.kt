import isel.leic.utils.Time

object ScoreDisplay { // Controla o mostrador de pontuação.

    // Inicializa a classe, estabelecendo os valores iniciais.
    fun init() {
        SerialEmitter.init() // Inicializa o emissor serial.
        //off(true)
        //off(false) // Liga o display de pontuação.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010101, 7) //85  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011100, 7) //92  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100011, 7) //99  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101010, 7) //106  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110001, 7) //113  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010000, 7) //80  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(100) // Espera 0,5 segundos antes de desligar o display.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011101, 7) //93  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100100, 7) //100  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101011, 7) //107  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110010, 7) //114  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010001, 7) //81  5º apagado
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011000, 7) //88  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(200) // Espera 0,5 segundos antes de desligar o display.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100101, 7) //93  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101100, 7) //100  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110011, 7) //107  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010010, 7) //114  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011001, 7) //81  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100000, 7) //88  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(200) // Espera 0,5 segundos antes de desligar o display.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101101, 7) //93  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110100, 7) //100  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010011, 7) //107  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011010, 7) //114  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100001, 7) //81  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101000, 7) //88  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(200) // Espera 0,5 segundos antes de desligar o display.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110101, 7) //93  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010100, 7) //100  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011011, 7) //107  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100010, 7) //114  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101001, 7) //81  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110000, 7) //88  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(200) // Espera 0,5 segundos antes de desligar o display.

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010101, 7) //85  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1011100, 7) //92  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1100011, 7) //99  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1101010, 7) //106  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1110001, 7) //113  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1010000, 7) //80  6º Initial

        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        Time.sleep(100) // Espera 0,5 segundos antes de desligar o display.

        // Nos ultimos bits escrevo o sitio onde vou escrever
        // Nos outros 4 bits digo o que escrevo, se for acima de 0b1010100 e abaixo de 0b1110100 escreve coisas estranhas

    }

    fun intplaying(){
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1111101, 7) //85  1º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1111100, 7) //92  2º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1111011, 7) //99  3º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1111010, 7) //106  4º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b1111001, 7) //113  5º
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000000, 7) //80  6º Initial



        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
    }

    // Envia comando para atualizar o valor do mostrador de pontuação.
    fun setScore(value: Int) {
        // Formata o valor com 5 dígitos, preenchendo com zeros à esquerda.
        val size = value.toString().length
        val formattedValue = value.toString().padStart(6, '0')
        var count = 7
        var num = 5
        var data = 0b1111101
        // Itera sobre cada caractere do valor formatado.
        for (i in formattedValue) {
            // Converte o caractere em um dígito inteiro.
            val bin = i.digitToInt()
            // Calcula o valor binário a ser enviado.
            val vall = bin.shl(3) + num
            // Envia o valor binário para o display de pontuação.
            SerialEmitter.send(SerialEmitter.Destination.SCORE, vall, 7)
            if ((i == '0' && count > size) || value == 0){
                SerialEmitter.send(SerialEmitter.Destination.SCORE, data, 7) //85  1º
                SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
            }
            count--
            data --
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
        Time.sleep(200)
        ScoreDisplay.off(true) // Desliga o display de pontuação.
        Time.sleep(200)
        ScoreDisplay.off(false) // Desliga o display de pontuação.
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
    }

    Time.sleep(2000) // Espera 2 segundos antes de desligar o display.
    ScoreDisplay.off(true) // Desliga o display de pontuação.
}
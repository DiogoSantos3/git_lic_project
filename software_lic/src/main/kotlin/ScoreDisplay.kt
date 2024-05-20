import isel.leic.utils.Time

object ScoreDisplay { // Controla o mostrador de pontuação.

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init() {

        SerialEmitter.init()
        off(false)

    }

    // Envia comando para atualizar o valor do mostrador de pontuação
    fun setScore(value: Int) { //ainda n esta validado
        val tamanho = value.toString().length
        var number = 5
        var count = 0
        while (count < tamanho) {
            val bin = value.toString()[count].digitToInt()
            val valor = bin.shl(3) + number
            SerialEmitter.send(SerialEmitter.Destination.SCORE, valor,7)
            number--
            count+=1
        }
    }

    // Envia comando para desativar/ativar a visualização do mostrador de pontuação
    fun off(value: Boolean) {
        if (value) {
            SerialEmitter.send(addr = SerialEmitter.Destination.SCORE, 0b0001111 , 7)

            //tem haver com a tabela do projeto Tabela1 - Modulo Score Display
        } else {
            SerialEmitter.send(addr = SerialEmitter.Destination.SCORE, 0b0000111, 7)

        }

    }
}
fun main() {
    ScoreDisplay.init()

    var count = 0
    var x = 0

    while (count != 10){
        x = 1 + count
        Time.sleep(1000)
        ScoreDisplay.setScore(0x0000+x)
        SerialEmitter.send(SerialEmitter.Destination.SCORE,0b0000110,7) //Update Display

        count += 1

    }
    ScoreDisplay.off(true)


}
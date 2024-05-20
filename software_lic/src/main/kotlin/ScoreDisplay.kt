import isel.leic.utils.Time

object ScoreDisplay { // Controla o mostrador de pontuação.

    // Inicia a classe, estabelecendo os valores iniciais.
    fun init() {
        SerialEmitter.init()
        off(false)
    }

    // Envia comando para atualizar o valor do mostrador de pontuação


    fun setScore(value: Int) { //Não validado
        val size = value.toString().length //Diz nos o número de digitos de 'value'
        var num = 5
        var count = 0

        while (count < size) {//Itera sobre cada dígito do 'value'.

            val bin = value.toString()[count].digitToInt()//Convertemos o caractere na posição count da string, dá o valor do dígito atual.

            val vall = bin.shl(3) + num

            SerialEmitter.send(SerialEmitter.Destination.SCORE, vall,7)
            num--
            count+=1
        }
    }

    // Envia comando para desativar/ativar a visualização do mostrador de pontuação
    fun off(value: Boolean) {
        val data = if (value) 0b0001111 else 0b0000111
        SerialEmitter.send(addr = SerialEmitter.Destination.SCORE, data, 7)
    }

}
fun main() {
    ScoreDisplay.init()

    var count = 0
    var x = 0


    while (count != 10){
        x =1 +  count
        Time.sleep(1000)
        ScoreDisplay.setScore(0x000+x)
        SerialEmitter.send(SerialEmitter.Destination.SCORE,0b0000110,7) //Update Display

        count += 1

    }


    Time.sleep(2000)
    ScoreDisplay.off(true)


}
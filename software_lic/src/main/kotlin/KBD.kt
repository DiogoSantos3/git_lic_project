import isel.leic.utils.Time



object KBD {//Ler teclas. Métodos retornam ‘0’..’9’,’#’,’*’ ou NONE.

    const val KEY = 0X0F        //kbd.K[0-3] -> UsbPort.I[0-3]
    const val KACK_MASK = 0X80  //UsbPort.O7 -> kbd.ack
    const val KVAL_MASK = 0X10  //kbd.val -> UsbPort.I4
    const val NONE = ' '
    val keys: CharArray = charArrayOf('1', '4', '7', '*', '2', '5', '8', '0', '3', '6', '9', '#') //Keys on keyboard

    //Inicia a classe
    fun init() {
        HAL.init()
    }

    //Retorna de imediato a tecla premida ou NONE se nenhuma tecla tiver premida.
    fun getKey(): Char {

        //Se alguma tecla tiver a ser premida...
        if (HAL.isBit(KVAL_MASK)) {

            val tecla = HAL.readBits(KEY)//Lê a tecla premida
            HAL.setBits(KACK_MASK)

            //Se não houver tecla premida...
            if(!HAL.isBit(KVAL_MASK)) {
                HAL.clrBits(KACK_MASK) //Dá clear da mask ACK para iniciar um novo varrimento
                return keys[tecla] //Vai ver no array keys onde está o valor da tecla premida e retorna o mesmo
            }

        }
        return NONE
    }

    // Retorna a tecla pressionada antes do timeout em milissegundos acabar, ou NONE caso contrário.
    fun waitKey(timeout: Long): Char {
        val timeFinal = timeout + Time.getTimeInMillis() //Tempo atual em milisegundos + timeout
        var key = NONE

        //Vai buscar a tecla premida até o timeout chegar ao fim
        do {
            key = getKey()
        } while (Time.getTimeInMillis() < timeFinal && key == NONE)

        return key
    }
}


fun main(){
    KBD.init()
    KBD.waitKey(99999999999)
}
object SerialEmitter { // Envia tramas para os diferentes módulos Serial Receiver.
    val SSLCD_MASK = 0x01 //LCDset
    val SSCORE_MASK = 0x02 //SCOREset
    val SCLOCK_MASK = 0x10 //
    val SDX_MASK = 0x08
    enum class Destination { LCD, SCORE }

    // Inicia a classe
    fun init() {
        HAL.init()
        HAL.setBits(SSLCD_MASK)
        HAL.setBits(SSCORE_MASK)
        HAL.clrBits(SCLOCK_MASK)
    }

    // Envia uma trama para o SerialReceiver identificado o destino em addr,os bits de dados em
    // ‘data’ e em size o número de bits a enviar.
    fun send(addr: Destination, data: Int, size: Int) {
        val mask = if (addr == Destination.LCD) SSLCD_MASK else SSCORE_MASK
        var parity = 0
        val frame = data
        var bitMask = 0x01

        HAL.clrBits(mask) //"Transição descendente do LCDset"

        for (i in 0..<size) {

            if ((frame and bitMask) != 0) { //Verifica se existe um '1' em 'data'
                parity++
                HAL.setBits(SDX_MASK) //Bit atual dos dados está sendo transmitido.
            } else {
                HAL.clrBits(SDX_MASK) //Bit atual dos dados não vai ser transmitido
            }

            HAL.setBits(SCLOCK_MASK) //Dá um Clock
            HAL.clrBits(SCLOCK_MASK) //Dá um Clock

            bitMask = bitMask shl 1 //Dá shift para verificar o próximo bit
        }

        if (parity % 2 != 0) { //Verifica se a quantidade de '1's é par ou impar
            HAL.setBits(SDX_MASK) //Se for impar o bit de paridade é ajustado para 1, para garantir que o número total de '1's seja par na transmissão
        } else {
            HAL.clrBits(SDX_MASK)//Se for par o bit de paridade é ajustado para 0
        }

        HAL.setBits(SCLOCK_MASK) //Dá um Clock
        HAL.clrBits(SCLOCK_MASK) //Dá um Clock
        HAL.setBits(mask)//"Transição ascendente do LCDset" -> significa que a transmissão foi concluída
    }
}
    fun main(){
    HAL.init()
    SerialEmitter.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD,0x155,9)
}
object SerialEmitter { // Envia tramas para os diferentes módulos Serial Receiver.
    val SSLCD_MASK = 0x01
    val SCORESelMask = 0x02
    val SCLOCK_MASK = 0x10
    val SDX_MASK = 0x08

    enum class Destination { LCD, SCORE }

    // Inicia a classe
    fun init() {
        HAL.init()
        HAL.setBits(SSLCD_MASK)
        HAL.setBits(SCORESelMask)
        HAL.clrBits(SCLOCK_MASK)
    }

    // Envia uma trama para o SerialReceiver identificado o destino em addr,os bits de dados em
// ‘data’ e em size o número de bits a enviar.
    fun send(addr: Destination, data: Int, size: Int) {
        val mask = if (addr == Destination.LCD) SSLCD_MASK else SCORESelMask
        var parity = 0
        val frame = data
        var bitMask = 0x01

        HAL.clrBits(mask)
        for (i in 0..<size) {
            if ((frame and bitMask) != 0) {
                parity++
                HAL.setBits(SDX_MASK)
            } else {
                HAL.clrBits(SDX_MASK)
            }
            HAL.setBits(SCLOCK_MASK)
            HAL.clrBits(SCLOCK_MASK)
            bitMask = bitMask shl 1
        }

        if (parity % 2 != 0) {
            HAL.setBits(SDX_MASK)
        } else {
            HAL.clrBits(SDX_MASK)
        }
        HAL.setBits(SCLOCK_MASK)
        HAL.clrBits(SCLOCK_MASK)
        HAL.setBits(mask)
    }
}
    fun main(){
    HAL.init()
    SerialEmitter.init()
    SerialEmitter.send(SerialEmitter.Destination.LCD,0x155,9)
}
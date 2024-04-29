
object SerialEmitter{ //Envia tramas para os diferentes módulos Serial Receiver

    enum class Destination{LCD,SCORE}

    private const val SSSCORE_MASK = 0x03
    private const val SSLCD_MASK = 0x00
    private const val SCLOCK_MASK = 0x01
    private const val SDX_MASK = 0x02

    //Inicia a class
    fun init(){
        HAL.init()
        HAL.setBits(SSLCD_MASK)
        HAL.setBits(SSLCD_MASK)
    }

    /*Envia tramas para o Serial Receiver identificando o destino em addr, os bits de dados
    em 'data' e em size o número de bits a enviar (calcular o bit de paridade???)*/
    fun send(addr: Destination, data: Int, size: Int) {
        val clrMask = if (addr == Destination.LCD) SSLCD_MASK else SSSCORE_MASK
        val setMask = if (addr == Destination.LCD) SSLCD_MASK else SSSCORE_MASK

        HAL.clrBits(clrMask)
        var update = data

        for(i in 0..<size) {
            HAL.clrBits(SCLOCK_MASK)
            HAL.writeBits(SDX_MASK, update)
            HAL.setBits(SCLOCK_MASK)
            update = update shr 1
        }

        HAL.clrBits(SCLOCK_MASK)
        HAL.setBits(setMask)
    }

}
fun main(){TODO()}
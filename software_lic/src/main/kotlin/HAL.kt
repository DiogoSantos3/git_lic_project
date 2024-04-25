import isel.leic.UsbPort



object HAL {//Virtualiza o acesso ao sistema UsbPort

    var last_Output = 0

    //Inicia a classe
    fun init () {
        UsbPort.write(last_Output)
    }

    //Identify Bits - Retorna true se o bit tiver o valor lógico '1'
    fun isBit (mask:Int) : Boolean {
        val read = UsbPort.read()
        return read.and(mask) != 0
    }

    //Read switches - Bits - Retorna os valores dos bits representados por mask presentes no UsbPort
    fun readBits(mask: Int): Int{
        val read = UsbPort.read()
        return read.and(mask)
    }

    //Mete os bits a '1' na mask - "turn on" leds
    fun setBits (mask: Int) {
        last_Output = mask.or(last_Output)
        UsbPort.write(last_Output)
    }

    //Bits da mask em '0' - clear bits
    fun clrBits(mask:Int) {
        last_Output = mask.inv().and(last_Output)
        UsbPort.write(last_Output)
    }

    //Escreve os bits na mask com o valor de value
    fun writeBits(mask: Int, value: Int) {
        clrBits(mask)
        setBits(value and mask)
    }


}

fun main(){
    HAL.init()
    while (true){
        //println(HAL.isBit(2))
        //println(HAL.readBits(3))
        //println(HAL.writeBits(7,3))
        //println(HAL.clrBits(3))
        //println(HAL.setBits(3))
    }
}

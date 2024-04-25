import isel.leic.utils.Time


object LCD{//Escreve no LCD usando a interface a 4 bits

    private const val LINES = 2                 //Dimensão do display - LINES
    private const val COLS = 16                 ////Dimensão do display - COLUMNS
    private const val SERIAL_INTERFACE = false
    private const val E_MASK = 0x20            //UsbPort.O5 -> lcd.e
    private const val RS_MASK = 0x10           //UsbPort.O4 -> lcd.rs
    private const val CLK_REG_MASK = 0x40      //UsbPort.O6 -> regLow.clk, regHigh.clk
    private const val DATA_MASK = 0x0F

    //Escreve um byte de comandos/dados no LCD em paralelo
    private fun writeByteParallel(rs: Boolean, data: Int) {
        //--------SIMPLE ERROR HERE----------
        val rs = if (rs) 1 else 0

        //Escolher se queremos uma mensagem de controlo ou de dados
        if (rs == 1){HAL.setBits(RS_MASK)}//Mensagem de dados
        else HAL.clrBits(RS_MASK)//Mensagem de controlo

        /*
        if (rs){HAL.setBits(RS_MASK)}
        else HAL.clrBits(RS_MASK)
         */

        HAL.setBits(E_MASK)              //"Ativa" o enable

        val high = data.shr(4)  //Shift para ficarmos só com os 4 bits de maior peso

        HAL.writeBits(DATA_MASK, high)   //Escreve os 4 bits de maior peso (?)
        HAL.setBits(CLK_REG_MASK)        //Clock1
        HAL.clrBits(CLK_REG_MASK)        //Clock1

        HAL.writeBits(DATA_MASK, data)   //Escrever os 4 bits de menor peso (?)
        HAL.setBits(CLK_REG_MASK)        //Clock2
        HAL.clrBits(CLK_REG_MASK)        //Clock2

        HAL.clrBits(E_MASK)              //"Desativa" o enable

    }

    //Escreve um byte de comandos/dados no LCD em série
    private fun writeByteSerial(rs:Boolean, data:Int){TODO()}

    //Escreve um byte de comandos/dados no LCD
    private fun writeByte(rs:Boolean, data:Int){
        if (SERIAL_INTERFACE) writeByteSerial(rs,data)
        else {writeByteParallel(rs,data)}
    }

    //Escrever mensagem de controlo
    private fun writeCMD(data:Int) {writeByte(false,data)}

    //Escrever mensagem de dados
    private fun writeDATA(data:Int){writeByte(true,data)}


    //Envia a sequencia de iniciação para a comunicação 4 bits (?)
    fun init(){
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
    }

    //Escreve um caráter na posição corrente
    fun write(c:Char){writeDATA(c.code)}

    //Escreve uma string na posição corrente
    fun write(text:String){
        for (element in text){
            write(element)}
    }

    //Envia comandos para posicionar o cursor
    fun cursor(line: Int, column: Int){
        writeCMD((line * 0X40 + column) or 0x80)
    }

    //Envia comandos para limpar o ecrã e posicionar o cursor no (0,0)
    fun clear(){
        writeCMD(0b00000001)
        cursor(0,0)
    }

}


fun main() {
    LCD.init()
    while (true) {
        LCD.clear()
        LCD.write("ola burro")
        Thread.sleep(2000)
    }
}
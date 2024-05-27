import isel.leic.utils.Time


object LCD{//Escreve no LCD usando a interface a 4 bits

    private const val LINES = 2                 //Dimensão do display - LINES
    private const val COLS = 16                 ////Dimensão do display - COLUMNS
    private const val SERIAL_INTERFACE = true
    private const val E_MASK = 0x20            //UsbPort.O5 -> lcd.e
    private const val RS_MASK = 0x10           //UsbPort.O4 -> lcd.rs
    const val CLK_REG_MASK = 0x40              //UsbPort.O6 -> regLow.clk, regHigh.clk
    private const val DATA_MASK = 0x0F

    //Escreve um byte de comandos/dados no LCD em paralelo
    private fun writeByteParallel(rs: Boolean, data: Int) {
        val sr = if (rs) 1 else 0

        //Escolher se queremos uma mensagem de controlo ou de dados
        if (sr == 1){HAL.setBits(RS_MASK)}//Mensagem de dados
        else HAL.clrBits(RS_MASK)//Mensagem de controlo

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
    private fun writeByteSerial(rs:Boolean, data:Int){
            val sr = if (rs) 1 else 0
            SerialEmitter.send(SerialEmitter.Destination.LCD,data.shl(1) + sr ,9)
    }

    //Escreve um byte de comandos/dados no LCD
    fun writeByte(rs:Boolean, data:Int){
        if (SERIAL_INTERFACE) writeByteSerial(rs,data)
        else {writeByteParallel(rs,data)}
    }

    //Escrever mensagem de controlo
    private fun writeCMD(data:Int) {writeByte(false,data)}

    //Escrever mensagem de dados
    private fun writeDATA(data:Int){writeByte(true,data)}


    //Envia a sequencia de iniciação para a comunicação 4 bits (?)
    fun init(){
        SerialEmitter.init()
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
        clear()
        cursor(0, 0)
    }
    /**
    fun init() {

    writeCMD(0b00110000)// Function set: Interface is 8 bits long (00110000 in binary)
    Time.sleep(15)// Wait
    writeCMD(0b00110000)// Function set: Repeated to ensure the LCD is properly initialized (00110000 in binary)
    Time.sleep(1)// Wait
    writeCMD(0b00110000)// Function set: Repeated to ensure the LCD is properly initialized (00110000 in binary)
    writeCMD(0b00111000)// Function set: Interface is 8 bits long, 2 lines, 5x8 dots (00111000 in binary)
    writeCMD(0b00001000)// Display off: Turn off display (00001000 in binary)
    writeCMD(0b00000001)// Display clear: Clear display and set DDRAM address to 0 (00000001 in binary)
    writeCMD(0b00000110)// Entry mode set: Increment mode, no display shift (00000110 in binary)
    writeCMD(0b00001111)// Display on/off control: Display on, cursor on, blink on (00001111 in binary)
    clear()// Clear display (this is redundant here as we already cleared the display above, but included for completeness)
    cursor(0, 0)// Set cursor to home position (0, 0)
    }

     */


    //Escreve um caráter na posição corrente
    fun write(c:Char){writeDATA(c.code)}

    //Escreve uma string na posição corrente
    fun write(text:String){
        for (element in text){
            write(element)}
    }

    //Envia comandos para posicionar o cursor
    fun cursor(line: Int, row: Int){
        writeCMD((line * 0X40 + row) or 0x80)
    }

    //Envia comandos para limpar o ecrã e posicionar o cursor no (0,0)
    fun clear(){
        writeCMD(0b00000001)

    }

}


fun main() {
    LCD.init()
    while (true) {
        LCD.clear()
        LCD.write("Hello World")
        Thread.sleep(2000)
    }
}
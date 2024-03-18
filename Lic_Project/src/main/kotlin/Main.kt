import isel.leic.UsbPort

fun main(args: Array<String>) {
    init()
    while(true){

        val value = UsbPort.read()
        lastOutpt = value
        UsbPort.write(value)
        //isBit(2)
        setBits(2)
        clrBits(2)
    }
}
var lastOutpt = 0
fun init(){
    UsbPort.write(lastOutpt)
}

fun readBits(mask:Int):Int{
    val input = UsbPort.read()
    return input and mask
}

fun isBit(mask:Int):Boolean{
    val input = UsbPort.read()
    var b = 0
    b = input and mask
    if (b != 0){
        print("TRUE\n")
        return true
    }
    print("FALSE\n")
    return false
}

fun setBits(mask:Int){
    val last = lastOutpt
    UsbPort.write(last or mask)

}

fun clrBits(mask:Int){
    val last = lastOutpt
    UsbPort.write(  mask.inv() and last)
}

fun writeBits(mask:Int, value:Int){
    val last = lastOutpt
    var a = 0
    a = last and mask
    a = a.toString()
    a = a.drop(value)
}
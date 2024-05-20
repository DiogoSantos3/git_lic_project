fun checkParity(data: Int) : Int {
    var i = 0
    var STR = data.toString()
    for (j in 0 until STR.length){
        if ( STR[j] == '1' ) i ++
    }
    println(i)
    if(i % 2 == 0) {
        STR ='1' + STR
        println (STR)
    }
    else {
        STR = '0' + STR
        println (STR)
    }
    val Date = STR.toInt()
    return Date
}

/*
fun checkParity(data: Int) : Boolean {
    val binaryString = Integer.toBinaryString(data)
    val numberOfOnes = binaryString.count { it == '1' }
    return if (numberOfOnes % 2 == 0) True else False
}
 */
fun main(){
    checkParity(0b100)
}
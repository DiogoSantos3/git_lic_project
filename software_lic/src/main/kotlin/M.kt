object M {


    private const val M_MASK = 0x80

    fun isM():Boolean{
        if (HAL.isBit(M_MASK)) {
            println("ON")
            return true
        }
        println("OFF")
        return false
    }

}
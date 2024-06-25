object M {
    private const val M_MASK = 0x80

    fun isM():Boolean{
        if (HAL.isBit(M_MASK)) {
            return true
        }
        return false
    }

}
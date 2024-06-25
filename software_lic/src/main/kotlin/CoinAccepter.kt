object CoinAccepter{
    private var lastState : Boolean = false
    private var COIN_MASK = 0x40
    var coin = 0
    fun isCoin():Boolean{

        if(HAL.isBit(COIN_MASK)){
            if (!lastState){

                HAL.setBits(COIN_MASK)
            }
            HAL.clrBits(COIN_MASK)

            return true
        }

        return false
    }

    fun countCoin():Int{

        if (isCoin())coin++
        return coin

    }
}
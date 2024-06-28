object CoinAccepter{
    private var lastState : Boolean = true
    private var COIN_MASK = 0x40
    var coin = 0
    fun isCoin():Boolean{

        if(!HAL.isBit(COIN_MASK) && lastState ){
            lastState = false
            println("NO COIN")

        }

        if (HAL.isBit(COIN_MASK) && !lastState ){
            lastState= true

            println("NO COIN 2")
            return true



        }


        return false
    }


}
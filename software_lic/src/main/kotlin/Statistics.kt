object Statistics {

    fun numCoins():String{
        return FilesAccess.ler("x.txt")[0]
    }
    fun numGames():String{
        return FilesAccess.ler("x.txt")[1]
    }
    fun addCoins(numCoin:Int){
        FilesAccess.escrever((numCoin+2).toString(),numGames())
    }

    fun addGames(numGames:Int){
        FilesAccess.escrever(numCoins(),numGames())
    }

    fun resetCounting(){
        FilesAccess.escrever(0.toString(),0.toString())
    }
}
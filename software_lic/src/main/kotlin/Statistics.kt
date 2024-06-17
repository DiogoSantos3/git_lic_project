import java.io.File

object Statistics {

    fun numCoins():String{
        return FilesAccess.ler("x.txt")[0]
    }
    fun numGames():String{
        return FilesAccess.ler("x.txt")[1]
    }

    fun addCoins(numCoin:Int){
        FilesAccess.escrever(numCoin+2,0)
    }

}
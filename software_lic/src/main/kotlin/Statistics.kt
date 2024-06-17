import java.io.File

object Statistics {

    fun numCoins():String{
        return FilesAccess.ler("x.txt")[0]
    }

    fun addCoins(numCoin:Int){
        FilesAccess.escrever(numCoin+2,0)
    }

}
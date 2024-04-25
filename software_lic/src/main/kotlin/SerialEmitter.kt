
object SerialEmitter{ //Envia tramas para os diferentes módulos Serial Receiver

    enum class Destination{LCD,SCORE}

    //Inicia a class
    fun init(){TODO()}

    /*Envia tramas para o Serial Receiver identificando o destino em addr, os bits de dados
    em 'data' e em size o número de bits a enviar*/
    fun send(addr:Destination, data:Int, size:Int){TODO()}

}
fun main(){}
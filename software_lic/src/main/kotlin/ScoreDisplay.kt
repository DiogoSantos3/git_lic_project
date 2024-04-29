
object ScoreDisplay {//Controla o mostrador de pontuação
    private const val SIZE = 7

    //Inicia a classe, estabelecendo valores iniciais
    fun init(){
        SerialEmitter.send(SerialEmitter.Destination.SCORE,0,SIZE)
    }

    //Envia comando para atualizar o valor do mostrador de pontuação
    fun setScore(value:Int){
        SerialEmitter.send(SerialEmitter.Destination.SCORE,value,SIZE)
    }

    //Envia comandos para desativar/ativar a visualização do mostrador de pontuação
    fun off(){TODO()}

}
fun main(){TODO()}
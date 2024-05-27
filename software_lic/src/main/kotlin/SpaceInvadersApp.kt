import isel.leic.utils.Time
import java.io.File

object SpaceInvadersApp {
    const val NAME_FILE = "statistics.txt"
    private var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    private var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    private var score: Int = 0
    private var coin: Int = 0
    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)
    private var line = 0
    private const val row = 1
    private var shootingKey = ' ' // Inicializa a tecla de tiro
    private var hit = false
    private const val interval = 1000L // Intervalo de tempo para gerar novos números aleatórios (1000 ms)
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)
    var COIN_MASK = 0x40
    val text = File(SpaceInvadersApp.NAME_FILE).readLines()

    fun isCoin(){
        if (HAL.isBit(COIN_MASK)){
            println(coin + 1)
            HAL.clrBits(COIN_MASK)
        }
    }

    fun displayStatistics() {
        // Criar uma lista de pares (nome, pontuação)
        val scores = text.map {
            val (name, score) = it.split(";")
            name to score.toInt()
        }.sortedByDescending { it.second } // Ordenar pela pontuação em ordem decrescente

        var position = 1

        for ((name, score) in scores) {
            isCoin()
            if (KBD.getKey() == '*') {
                LCD.clear()
                playing()
                return
            }
            LCD.cursor(1, 0)
            LCD.write(" $position $name $score           ")
            position++
            Time.sleep(1000)
        }
    }

    // Função para inicializar os componentes
    fun init() {
        TUI.init()
        ScoreDisplay.init()
        LCD.write(" Space Invaders ") // Escreve a mensagem inicial na primeira linha do LCD
        LCD.cursor(1, 0)
        LCD.write(" Game X  X X  0$ ") // Escreve uma segunda mensagem na segunda linha do LCD
        Time.sleep(2000)
    }

    fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 1) // Atualiza o display com o valor atual de x.
        SerialEmitter.send(SerialEmitter.Destination.SCORE, 0b0000110, 7) // Atualiza o display.
        return score + 1
    }

    fun addInvaders(randomLine: Int, randomNumber: String) {
        if (randomLine == 0) {
            list0 += randomNumber
        } else {
            list1 += randomNumber
        }
        TUI.displayWrite(list0, list1, randomLine, 1, hit)
    }

    fun updateTime() {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastUpdateTime >= interval) {
            randomLine = (0..1).random()
            randomNumber = (0..9).random().toString()
            addInvaders(randomLine, randomNumber) //Add random invaders
            lastUpdateTime = currentTime
        }
    }

    fun changeLine() {
        LCD.cursor(line, row)
        LCD.write(" ")
        line = if (line == 0) 1 else 0
        TUI.showGun(line, row)
        LCD.cursor(line, row)
    }

    fun checkHit() {
        if (line == 0 && list0.isNotEmpty() && shootingKey == list0[0]) {
            score = addScore(score)
            list0 = list0.substring(1)
            TUI.displayWrite(list0, list1, line, 1, hit = true)
        } else if (line == 1 && list1.isNotEmpty() && shootingKey == list1[0]) {
            score = addScore(score)
            list1 = list1.substring(1)
            TUI.displayWrite(list0, list1, line, 1, hit = true)
        }
    }

    fun displayKey(key: Char) {
        if (key != KBD.NONE) {
            LCD.cursor(line, 0)
            LCD.write(key)
            shootingKey = key
        }
    }

    fun handleKeyPress(key: Char) {
        when (key) {
            '*' -> changeLine()
            '#' -> checkHit()
            else -> displayKey(key)
        }
    }

    // Função principal do jogo
    fun playing() {
        TUI.displayBars() // Exibe as barras no início das linhas
        TUI.showGun(line, row)
        while (list0.length < 14 && list1.length < 14) { // Loop principal do jogo
            updateTime()
            handleKeyPress(KBD.getKey())
            Time.sleep(100) //Delay
        }

        TUI.gameOver(score * 10) // Exibe a tela de fim de jogo com a pontuação 0
    }
}

// Função principal para iniciar o jogo
fun main() {
    SpaceInvadersApp.init()
    while (true) {
        SpaceInvadersApp.displayStatistics()
        SpaceInvadersApp.isCoin()
        if (KBD.getKey() == '*') {
            LCD.clear()
            SpaceInvadersApp.playing()
            break
        }
    }
}

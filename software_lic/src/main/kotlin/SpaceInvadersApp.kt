import isel.leic.utils.Time // Importa a biblioteca Time para utilizar funções de tempo
import kotlin.system.exitProcess // Importa a função exitProcess para encerrar o programa

// Define um objeto singleton para o aplicativo Space Invaders
object SpaceInvadersApp {

    //========== VARIÁVEIS ==========
    // Estados possíveis do jogo
    enum class State { MANUTENCION, INITIAL, PLAYING, GAMEOVER }

    private var randomNumber = (0..9).random().toString() // Gera um número aleatório entre 0 e 9
    private var randomLine = (0..1).random() // Gera uma linha aleatória (0 ou 1)

    var state = State.INITIAL // Estado inicial do jogo

    var list0: String = "" // Lista para armazenar números aleatórios na linha 0
    var list1: String = "" // Lista para armazenar números aleatórios na linha 1
    var score: Int = 0 // Variável para armazenar a pontuação
    var shootingKey = ' ' // Inicializa a tecla de tiro
    private var hit = false // Variável para indicar se houve um acerto

    private const val SHOW_INVADERS_INTERVAL = 1000L // Intervalo de tempo para gerar novos números aleatórios
    private var lastUpdateTime = System.currentTimeMillis() // Marca o tempo da última atualização (Começo do jogo)

    private var cursor = TUI.Cursor() // Inicializa o cursor da TUI
    private val topScore = Scores.getTopScore() // Obtém a maior pontuação
    private val lastScore = Scores.getLastScore() // Obtém a última pontuação

    //===================================================
    // Função para inicializar os componentes
    fun init() {
        TUI.init() // Inicializa a interface de usuário baseada em texto
    }

    // Função para alternar o display de pontuação ligado e desligado
    private fun scoreOnOff(){
        repeat(10) { // Repete 10 vezes
            Time.sleep(300) // Espera 300 milissegundos
            ScoreDisplay.off(true) // Desliga o display de pontuação
            Time.sleep(300) // Espera 300 milissegundos
            ScoreDisplay.off(false) // Liga o display de pontuação
        }
    }

    // Função para adicionar pontos à pontuação atual
    private fun addScore(score: Int): Int {
        ScoreDisplay.setScore(score + 4) // Atualiza o display com o valor atual de score
        return score + 4 // Retorna a nova pontuação
    }

    // Função para alternar a linha do cursor
    private fun changeLine() {
        cursor.displayBars() // Exibe as barras no cursor
        var linee = cursor.line // Obtém a linha atual do cursor
        cursor.write(linee, cursor.row, " ") // Apaga o conteúdo na posição atual do cursor
        linee = if (cursor.line == 0) 1 else 0 // Alterna a linha do cursor
        cursor.showGun(linee, cursor.row) // Exibe a arma na nova linha
        cursor = TUI.Cursor(linee, cursor.row) // Atualiza a posição do cursor
    }

    // Função para verificar se houve um acerto
    private fun checkHit() {
        if (cursor.line == 0 && list0.isNotEmpty() && shootingKey == list0[0]) { // Verifica se há um acerto na linha 0
            score = SpaceInvadersApp.addScore(score) // Adiciona pontos à pontuação
            list0 = list0.substring(1) // Remove o primeiro caractere da lista
            TUI.displayWrite(list0.drop(0), list1, cursor.line, 1, hit = true) // Atualiza o display
        } else if (cursor.line == 1 && list1.isNotEmpty() && shootingKey == list1[0]) { // Verifica se há um acerto na linha 1
            score = SpaceInvadersApp.addScore(score) // Adiciona pontos à pontuação
            list1 = list1.substring(1) // Remove o primeiro caractere da lista
            TUI.displayWrite(list0, list1.drop(0), cursor.line, 1, hit = true) // Atualiza o display
        }
    }

    // Função para atualizar o tempo e gerar novos invasores
    private fun updateTime() {
        val currentTime = System.currentTimeMillis() // Obtém o tempo atual
        if (currentTime - lastUpdateTime >= SHOW_INVADERS_INTERVAL) { // Verifica se passou o intervalo de tempo definido
            randomLine = (0..1).random() // Gera uma nova linha aleatória
            randomNumber = (0..9).random().toString() // Gera um novo número aleatório
            addInvaders(randomLine, randomNumber) // Adiciona invasores aleatórios
            lastUpdateTime = currentTime // Atualiza o tempo da última atualização
        }
    }

    // Função para exibir a tecla pressionada no display
    private fun displayKey(key: Char, line: Int) {
        if (key != TUI.NONE) { // Verifica se a tecla é diferente de NONE
            cursor.write(line, 0, key.toString()) // Escreve a tecla na posição do cursor
            shootingKey = key // Atualiza a tecla de tiro
        }
    }

    // Função principal do jogo
    fun playing() {
        cursor.displayBars() // Exibe as barras no início das linhas
        cursor.showGun(cursor.line, cursor.row) // Exibe a posição inicial da arma
        ScoreDisplay.intplaying() // Inicializa a exibição da pontuação durante o jogo
        while (list0.length < 14 && list1.length < 14) { // Loop principal do jogo até as listas atingirem o comprimento 14
            updateTime() // Atualiza o tempo e gera novos invasores
            when (val key: Char = TUI.getKey()) { // Lê a tecla pressionada
                '*' -> changeLine() // Alterna a linha do cursor se a tecla '*' for pressionada
                '#' -> checkHit() // Verifica acertos se a tecla '#' for pressionada
                else -> displayKey(key, cursor.line) // Exibe a tecla pressionada no display
            }
            Time.sleep(100) // Delay de 100 milissegundos
        }
        Statistics.addGames() // Adiciona um jogo às estatísticas
        Statistics.addCoins(false) // Adiciona moedas às estatísticas
        list0 = "" // Reseta a lista 0
        list1 = "" // Reseta a lista 1
        state = State.GAMEOVER // Altera o estado para GAMEOVER
    }

    // Função para o estado de manutenção
    fun manutencion() {
        while (M.isM()) { // Loop enquanto o motor está em manutenção
            cursor.write(0, 0, " On Maintenance ") // Escreve "On Maintenance" na linha 0
            cursor.write(1, 0, "*-Count  #-ShutD  ") // Escreve opções de manutenção na linha 1
            while (true) { // Loop para processar teclas durante a manutenção
                val key: Char = TUI.getKey() // Lê a tecla pressionada
                if (key in '0'..'9') { // Se for um número, sai do modo de manutenção
                    TUI.clear() // Limpa a tela
                    state = State.PLAYING // Altera o estado para PLAYING
                    return
                }
                when (key) {
                    '*' -> { // Se a tecla '*' for pressionada
                        displayStats() // Exibe estatísticas
                        while (true) { // Loop para processar teclas durante a exibição de estatísticas
                            val innerKey: Char = TUI.getKey() // Lê a tecla pressionada
                            if (innerKey in '0'..'9' || innerKey == '*') { // Se for um número ou '*', retorna ao modo de manutenção
                                state = State.MANUTENCION
                                return
                            } else if (innerKey == '#') { // Se a tecla '#' for pressionada
                                cursor.write(0, 0, " Reset Counting?         ") // Escreve "Reset Counting?" na linha 0
                                cursor.write(1, 0, " 5-Yes other-No  ") // Escreve opções de reset na linha 1
                                while (true) { // Loop para processar teclas durante o reset
                                    val shutdownKey: Char = TUI.getKey() // Lê a tecla pressionada
                                    if (shutdownKey == '5') { // Se a tecla '5' for pressionada, reseta as estatísticas
                                        Statistics.resetCounting()
                                        state = State.MANUTENCION
                                        return
                                    } else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') { // Se for outra tecla, retorna ao modo de manutenção
                                        state = State.MANUTENCION
                                        return
                                    }
                                }
                            }
                        }
                    }
                    '#' -> { // Se a tecla '#' for pressionada
                        cursor.write(0, 0, "    Shut Down?         ") // Escreve "Shut Down?" na linha 0
                        cursor.write(1, 0, " 5-Yes other-No   ") // Escreve opções de desligamento na linha 1
                        while (true) { // Loop para processar teclas durante o desligamento
                            val shutdownKey: Char = KBD.getKey() // Lê a tecla pressionada
                            if (shutdownKey == '5') { // Se a tecla '5' for pressionada, encerra o programa
                                exitProcess(0)
                            } else if (shutdownKey in '0'..'9' || shutdownKey == '*' || shutdownKey == '#') { // Se for outra tecla, retorna ao modo de manutenção
                                state = State.MANUTENCION
                                return
                            }
                        }
                    }
                }
            }
        }
    }

    // Função para exibir estatísticas de manutenção
    private fun displayStats() {
        cursor.write(0, 0, " Coins:${Statistics.numCoins()}          ") // Escreve o número de moedas na linha 0
        cursor.write(1, 0, " Games:${Statistics.numGames()}         ") // Escreve o número de jogos na linha 1
    }

    // Função para exibir estatísticas de pontuação
    fun displayStatistics() {
        if (state == State.INITIAL) { // Se o estado for INITIAL
            score = 0 // Reseta a pontuação
            ScoreDisplay.setScore(score) // Atualiza o display de pontuação
            val cores = Scores.splitScores().take(20) // Obtém as 20 melhores pontuações
            var position = 1 // Inicializa a posição na lista
            for ((name, score) in cores) { // Loop para exibir cada pontuação
                val numCoins = Statistics.numCoins().toInt() // Obtém o número de moedas
                if (M.isM()) { // Se o motor está em manutenção, altera o estado
                    state = State.MANUTENCION
                    break
                }
                if (CoinAccepter.isCoin()) { // Se uma moeda for inserida
                    Statistics.addCoins(true) // Adiciona a moeda às estatísticas
                    if (Statistics.numCoins().toInt() <= 9) {
                        lcd_segunda_linha() // Atualiza a segunda linha do LCD
                        ScoreDisplay.init() // Inicializa a exibição da pontuação
                    } else {
                        lcd_segunda_linha() // Atualiza a segunda linha do LCD
                        ScoreDisplay.init() // Inicializa a exibição da pontuação
                    }
                }
                if (TUI.getKey() == '*' && numCoins >= 2) { // Se a tecla '*' for pressionada e houver moedas suficientes
                    TUI.clear() // Limpa a tela
                    state = State.PLAYING // Altera o estado para PLAYING
                    break
                }
                ScoreDisplay.init() // Inicializa a exibição da pontuação
                TUI.cursor.write(0, 0, " Space Invaders ") // Escreve "Space Invaders" na linha 0
                cursor.write(1, 0, namewithscore(position, name, score)) // Escreve o nome e a pontuação na linha 1
                ScoreDisplay.init() // Inicializa a exibição da pontuação
                if (Statistics.numCoins().toInt() <= 9) {
                    lcd_segunda_linha() // Atualiza a segunda linha do LCD
                    ScoreDisplay.init() // Inicializa a exibição da pontuação
                } else {
                    lcd_segunda_linha() // Atualiza a segunda linha do LCD
                    ScoreDisplay.init() // Inicializa a exibição da pontuação
                }
                if (Scores.num_of_players == position) { // Se percorreu toda a lista de jogadores, reseta a posição
                    position = 0
                }
                position++
                for (i in 1..5) { // Loop para verificação de moedas durante a espera
                    if (M.isM()) { // Se o motor está em manutenção, altera o estado
                        state = State.MANUTENCION
                        break
                    }
                    if (CoinAccepter.isCoin()) { // Se uma moeda for inserida
                        Statistics.addCoins(true) // Adiciona a moeda às estatísticas
                        if (Statistics.numCoins().toInt() <= 9) {
                            lcd_segunda_linha() // Atualiza a segunda linha do LCD
                            ScoreDisplay.init() // Inicializa a exibição da pontuação
                        } else {
                            lcd_segunda_linha() // Atualiza a segunda linha do LCD
                            ScoreDisplay.init() // Inicializa a exibição da pontuação
                        }
                    }
                }
            }
        }
    }

    // Função para formatar nome e pontuação
    private fun namewithscore(position: Int, name: String, score: String): String {
        var namewithscore = "$position-$name" // Formata a posição e o nome
        var space = 13 - (name.length + score.length) // Calcula o espaço restante
        for (i in 0..space) { // Adiciona espaços
            namewithscore += " "
        }
        namewithscore += score // Adiciona a pontuação
        return namewithscore
    }

    // Função para formatar string com moeda
    private fun initwithcoin(coin: String): String {
        var returns = " Game X  X X" // Inicializa a string com " Game X  X X"
        var space = returns.length + coin.length + 1 // Calcula o espaço ocupado pela string atual mais o comprimento da moeda e um espaço adicional
        space = 16 - space // Calcula o número de espaços restantes para preencher a linha de 16 caracteres
        for (i in 0..space) { // Adiciona espaços até preencher o comprimento necessário
            returns += " "
        }
        returns += "$coin" // Adiciona a string da moeda ao final
        return returns // Retorna a string formatada
    }

    // Função para exibir a segunda linha do LCD
    fun lcd_segunda_linha() {
        TUI.cursor.write(1, 0, initwithcoin(Statistics.numCoins())) // Escreve na segunda linha do LCD a string formatada com o número de moedas
        TUI.cursor.special_characters() // Imprime caracteres especiais no cursor
    }

    // Função para adicionar invasores
    private fun addInvaders(randomLine: Int, randomNumber: String) {
        if (randomLine == 0) { // Se a linha aleatória for 0
            list0 += randomNumber // Adiciona o número aleatório à lista da linha 0
        } else { // Se a linha aleatória for 1
            list1 += randomNumber // Adiciona o número aleatório à lista da linha 1
        }
        TUI.displayWrite(list0, list1, randomLine, 1, hit) // Atualiza o display com as listas de números e a linha atingida
    }

    // Função para exibir a tela de game over
    fun gameOover() {
        cursor.write(0, 0, "*** GAME OVER ***") // Escreve "GAME OVER" na primeira linha do display
        cursor.write(1, 0, " Score: ${SpaceInvadersApp.score}            ") // Escreve a pontuação final na segunda linha do display
        if (score > lastScore?.toInt()!! && score != 0) { // Se a pontuação atual for maior que a última pontuação e não for zero
            scoreOnOff() // Executa a animação de ligar e desligar o display de pontuação
            val newName = TUI.newScore() // Obtém um novo nome do jogador
            Scores.writePlayers(newName, (score).toString()) // Escreve o nome e a pontuação do jogador
            state = State.INITIAL // Altera o estado para INITIAL
            return // Sai da função
        } else {
            scoreOnOff() // Executa a animação de ligar e desligar o display de pontuação
            state = State.INITIAL // Altera o estado para INITIAL
            return // Sai da função
        }
    }
}

// Função main para inicializar e rodar o jogo
fun main() {
    SpaceInvadersApp.init() // Inicializa os componentes do aplicativo Space Invaders
    TUI.cursor.write(0, 0, " Space Invaders ") // Escreve "Space Invaders" na primeira linha da interface de usuário baseada em texto
    SpaceInvadersApp.lcd_segunda_linha() // Atualiza a segunda linha do display LCD com informações iniciais
    ScoreDisplay.init() // Inicializa a exibição da pontuação com uma animação

    while (true) { // Loop principal do jogo
        when (SpaceInvadersApp.state) { // Verifica o estado atual do jogo
            SpaceInvadersApp.State.INITIAL -> { // Se o estado for INITIAL
                SpaceInvadersApp.displayStatistics() // Exibe as estatísticas de pontuação
            }
            SpaceInvadersApp.State.PLAYING -> { // Se o estado for PLAYING
                SpaceInvadersApp.playing() // Executa a função principal do jogo
            }
            SpaceInvadersApp.State.MANUTENCION -> { // Se o estado for MANUTENCION
                SpaceInvadersApp.manutencion() // Executa a função de manutenção
            }
            SpaceInvadersApp.State.GAMEOVER -> { // Se o estado for GAMEOVER
                SpaceInvadersApp.gameOover() // Executa a função de fim de jogo
            }
        }
    }
}

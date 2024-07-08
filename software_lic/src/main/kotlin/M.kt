object M { // Define um objeto chamado M

    private const val M_MASK = 0x80 // Declara uma constante privada M_MASK com o valor 0x80 (128 em decimal)

    // Função que verifica se o bit correspondente a M_MASK está definido
    fun isM(): Boolean {
        if (HAL.isBit(M_MASK)) { // Verifica se o bit correspondente a M_MASK está definido (retorna true se estiver definido)
            return true // Retorna true se o bit estiver definido
        }
        return false // Retorna false se o bit não estiver definido
    }
}

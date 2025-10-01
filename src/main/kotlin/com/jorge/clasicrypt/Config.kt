// Archivo: clasicrypt/Config.kt
package com.jorge.clasicrypt

/**
 * Configuración central para los algoritmos criptográficos
 * Controla el comportamiento de los cifrados respecto al alfabeto y formato
 */
data class Config(
    // Alfabeto que se utilizará para las operaciones
    val alfabeto: Alphabet = Alphabet.ESPAÑOL,

    // Si debe mantenerse el caso original (mayúsculas/minúsculas)
    val preservarMayusculas: Boolean = true,

    // Si se deben ignorar caracteres no presentes en el alfabeto
    val ignorarDesconocidos: Boolean = true
) {
    // Mapa interno para búsquedas rápidas de índices
    private val indice: Map<Char, Int> = alfabeto.caracteres.withIndex().associate { it.value to it.index }

    init {
        // Validaciones de integridad del alfabeto
        require(alfabeto.caracteres.isNotEmpty()) { "El alfabeto no puede estar vacío." }
        require(alfabeto.caracteres.toSet().size == alfabeto.caracteres.length) {
            "El alfabeto no debe repetir caracteres."
        }
    }

    /**
     * Busca el índice de un carácter en el alfabeto actual
     * @param c Carácter a buscar
     * @return Índice del carácter o -1 si no se encuentra
     */
    internal fun indiceDe(c: Char): Int {
        // Para alfabetos que no distinguen mayúsculas/minúsculas, normalizamos
        val caracterParaBuscar = when {
            alfabeto == Alphabet.COMPLETO -> c
            else -> c.uppercaseChar()
        }
        return indice[caracterParaBuscar] ?: -1
    }

    /**
     * Convierte un índice del alfabeto de vuelta a carácter, respetando el caso original
     * @param indice Posición en el alfabeto
     * @param esMinuscula Si el carácter original era minúscula
     * @return Carácter convertido respetando las reglas de caso
     */
    internal fun mapearSalida(indice: Int, esMinuscula: Boolean): Char {
        val ch = alfabeto.caracterEn(indice)
        // Solo convertir a minúscula si se debe preservar el caso y el alfabeto lo permite
        return if (preservarMayusculas && esMinuscula && alfabeto != Alphabet.COMPLETO) {
            ch.lowercaseChar()
        } else {
            ch
        }
    }

    /**
     * Normaliza un carácter de entrada para procesamiento
     * @param c Carácter de entrada
     * @return Par (índice, esMinuscula) para el carácter
     */
    internal fun normalizarEntrada(c: Char): Pair<Int, Boolean> {
        val esMinuscula = c.isLowerCase()
        val caracterParaBuscar = when {
            alfabeto == Alphabet.COMPLETO -> c
            else -> c.uppercaseChar()
        }
        return indiceDe(caracterParaBuscar) to esMinuscula
    }
}
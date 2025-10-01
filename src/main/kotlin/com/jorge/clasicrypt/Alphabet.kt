// Archivo: clasicrypt/Alphabet.kt
package com.jorge.clasicrypt

/**
 * Clase que representa diferentes alfabetos para los cifrados
 * Define conjuntos de caracteres válidos para operaciones criptográficas
 */
class Alphabet private constructor(
    val caracteres: String,
    val nombre: String
) {
    /**
     * @return Número de caracteres en el alfabeto
     */
    val tamaño: Int get() = caracteres.length

    /**
     * Busca el índice de un carácter en el alfabeto
     * @param caracter Carácter a buscar
     * @return Índice del carácter o -1 si no se encuentra
     */
    fun indiceDe(caracter: Char): Int = caracteres.indexOf(caracter)

    /**
     * Obtiene el carácter en una posición específica del alfabeto
     * @param indice Posición del carácter
     * @return Carácter en la posición indicada
     */
    fun caracterEn(indice: Int): Char = caracteres[indice]

    companion object {
        // Alfabeto español que incluye la Ñ
        val ESPAÑOL = Alphabet("ABCDEFGHIJKLMNÑOPQRSTUVWXYZ", "Español")

        // Alfabeto inglés estándar (sin Ñ)
        val INGLES = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ", "Inglés")

        // Alfabeto con letras mayúsculas y números
        val ALFANUMERICO = Alphabet("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789", "Alfanumérico")

        // Alfabeto completo con mayúsculas, minúsculas, números y puntuación básica
        val COMPLETO = Alphabet(
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 .,:;!?",
            "Completo"
        )

        /**
         * Obtiene un alfabeto por su nombre
         * @param nombre Nombre del alfabeto deseado
         * @return Instancia del alfabeto correspondiente
         */
        fun desdeNombre(nombre: String): Alphabet {
            return when (nombre) {
                "Español" -> ESPAÑOL
                "Inglés" -> INGLES
                "Alfanumérico" -> ALFANUMERICO
                "Completo" -> COMPLETO
                else -> ESPAÑOL // Valor por defecto si no se reconoce
            }
        }

        /**
         * @return Lista de nombres de alfabetos disponibles para la interfaz
         */
        fun nombresDisponibles(): List<String> =
            listOf("Español", "Inglés", "Alfanumérico", "Completo")
    }

    override fun toString(): String = nombre
}
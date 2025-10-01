// Archivo: clasicrypt/Vigenere.kt
package com.jorge.clasicrypt

/**
 * Implementación del cifrado Vigenère
 * Cifrado polialfabético que usa una clave repetida cíclicamente
 * Más seguro que cifrados monoalfabéticos como César
 */
class Vigenere(private val config: Config = Config()) : TextCipher<String> {

    /**
     * Cifra un texto plano usando el algoritmo Vigenère
     * @param textoPlano Texto original a cifrar
     * @param clave Clave que se repetirá cíclicamente
     * @return Texto cifrado
     */
    override fun cifrar(textoPlano: String, clave: String): String {
        // Validación de parámetros básicos
        require(clave.isNotBlank()) { "La clave Vigenère no puede estar vacía." }

        // Preprocesa la clave: convierte a mayúsculas y obtiene índices válidos
        val indicesClave = clave.map { it.uppercaseChar() }
            .mapNotNull { k -> config.indiceDe(k).takeIf { it >= 0 } }
        require(indicesClave.isNotEmpty()) { "La clave no contiene letras del alfabeto configurado." }

        val tamañoAlfabeto = config.alfabeto.tamaño
        val resultado = StringBuilder(textoPlano.length)
        var indiceClave = 0

        // Procesa cada carácter del texto plano
        for (caracter in textoPlano) {
            val (indicePlano, esMinuscula) = config.normalizarEntrada(caracter)

            // Manejo de caracteres fuera del alfabeto
            if (indicePlano < 0) {
                if (config.ignorarDesconocidos) {
                    resultado.append(caracter)
                } else {
                    error("Carácter '$caracter' fuera del alfabeto.")
                }
                continue
            }

            // Aplica el desplazamiento según la clave actual
            val desplazamiento = indicesClave[indiceClave % indicesClave.size]
            val nuevoIndice = (indicePlano + desplazamiento) % tamañoAlfabeto
            resultado.append(config.mapearSalida(nuevoIndice, esMinuscula))
            indiceClave++
        }

        return resultado.toString()
    }

    /**
     * Descifra un texto cifrado usando el algoritmo Vigenère
     * @param textoCifrado Texto cifrado a descifrar
     * @param clave Clave que se usó originalmente para cifrar
     * @return Texto plano original
     */
    override fun descifrar(textoCifrado: String, clave: String): String {
        // Validación de parámetros básicos (igual que en cifrar)
        require(clave.isNotBlank()) { "La clave Vigenère no puede estar vacía." }

        val indicesClave = clave.map { it.uppercaseChar() }
            .mapNotNull { k -> config.indiceDe(k).takeIf { it >= 0 } }
        require(indicesClave.isNotEmpty()) { "La clave no contiene letras del alfabeto configurado." }

        val tamañoAlfabeto = config.alfabeto.tamaño
        val resultado = StringBuilder(textoCifrado.length)
        var indiceClave = 0

        // Procesa cada carácter del texto cifrado
        for (caracter in textoCifrado) {
            val (indiceCifrado, esMinuscula) = config.normalizarEntrada(caracter)

            // Manejo de caracteres fuera del alfabeto
            if (indiceCifrado < 0) {
                if (config.ignorarDesconocidos) {
                    resultado.append(caracter)
                } else {
                    error("Carácter '$caracter' fuera del alfabeto.")
                }
                continue
            }

            // Aplica el desplazamiento inverso según la clave
            val desplazamiento = indicesClave[indiceClave % indicesClave.size]
            // Usa mod para manejar correctamente números negativos
            val indiceOriginal = (indiceCifrado - desplazamiento).mod(tamañoAlfabeto)
            resultado.append(config.mapearSalida(indiceOriginal, esMinuscula))
            indiceClave++
        }

        return resultado.toString()
    }
}
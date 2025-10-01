// Archivo: clasicrypt/Atbash.kt
package com.jorge.clasicrypt

/**
 * Implementación del cifrado Atbash
 * Cifrado de sustitución monoalfabético que invierte el alfabeto
 * El cifrado y descifrado son idénticos (involución)
 */
object Atbash : TextCipher<Unit> {

    /**
     * Transforma un texto aplicando la sustitución Atbash
     * @param texto Texto a transformar (puede ser plano o cifrado)
     * @param config Configuración con el alfabeto a usar
     * @return Texto transformado
     */
    private fun transformar(texto: String, config: Config): String {
        val tamañoAlfabeto = config.alfabeto.tamaño
        val resultado = StringBuilder(texto.length)

        // Procesa cada carácter del texto
        for (caracter in texto) {
            val (indice, esMinuscula) = config.normalizarEntrada(caracter)

            // Manejo de caracteres fuera del alfabeto
            if (indice < 0) {
                if (config.ignorarDesconocidos) {
                    resultado.append(caracter)
                } else {
                    error("Carácter '$caracter' fuera del alfabeto.")
                }
                continue
            }

            // Atbash: invierte el alfabeto (primero ↔ último)
            val nuevoIndice = tamañoAlfabeto - 1 - indice
            resultado.append(config.mapearSalida(nuevoIndice, esMinuscula))
        }

        return resultado.toString()
    }

    /**
     * Cifra un texto plano usando Atbash
     * @param textoPlano Texto original a cifrar
     * @param clave No se usa en Atbash (siempre Unit)
     * @return Texto cifrado
     */
    override fun cifrar(textoPlano: String, clave: Unit): String =
        transformar(textoPlano, Config())

    /**
     * Descifra un texto cifrado usando Atbash
     * @param textoCifrado Texto cifrado a descifrar
     * @param clave No se usa en Atbash (siempre Unit)
     * @return Texto plano original
     */
    override fun descifrar(textoCifrado: String, clave: Unit): String =
        transformar(textoCifrado, Config())

    /**
     * Versiones estáticas para compatibilidad con código existente
     */
    fun cifrar(textoPlano: String, config: Config = Config()): String =
        transformar(textoPlano, config)

    fun descifrar(textoCifrado: String, config: Config = Config()): String =
        transformar(textoCifrado, config)
}
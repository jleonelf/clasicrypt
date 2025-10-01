// Archivo: clasicrypt/TextCipher.kt
package com.jorge.clasicrypt

/**
 * Interfaz que define el contrato para todos los algoritmos criptográficos
 * @param K Tipo de clave que utiliza el algoritmo
 */
interface TextCipher<K> {
    /**
     * Cifra un texto plano usando una clave específica
     * @param textoPlano Texto original a cifrar
     * @param clave Clave para el cifrado
     * @return Texto cifrado resultante
     */
    fun cifrar(textoPlano: String, clave: K): String

    /**
     * Descifra un texto cifrado usando una clave específica
     * @param textoCifrado Texto cifrado a descifrar
     * @param clave Clave para el descifrado
     * @return Texto plano original
     */
    fun descifrar(textoCifrado: String, clave: K): String
}
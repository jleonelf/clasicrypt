// Archivo: Main.kt (opcional - para pruebas)
package com.jorge

import com.jorge.clasicrypt.*

/**
 * Función principal para pruebas en consola
 * Demuestra el uso de los cifrados Vigenère y Atbash
 */
fun main() {
    println("=== DEMOSTRACIÓN DE CIFRADOS CLÁSICOS ===")

    // Configuración con alfabeto español
    val configES = Config(Alphabet.ESPAÑOL, preservarMayusculas = true, ignorarDesconocidos = true)

    println("\n=== Vigenère (Español) ===")
    val vigenere = Vigenere(configES)
    val textoPlano = "HOLA MUNDO"
    val clave = "CLAVE"

    val textoCifrado = vigenere.cifrar(textoPlano, clave)
    val textoDescifrado = vigenere.descifrar(textoCifrado, clave)

    println("Texto plano: $textoPlano")
    println("Clave: $clave")
    println("Texto cifrado: $textoCifrado")
    println("Texto descifrado: $textoDescifrado")

    println("\n=== Atbash (Español) ===")
    val textoCifradoAtbash = Atbash.cifrar("HOLA MUNDO", configES)
    val textoDescifradoAtbash = Atbash.descifrar(textoCifradoAtbash, configES)

    println("Texto plano: HOLA MUNDO")
    println("Texto cifrado: $textoCifradoAtbash")
    println("Texto descifrado: $textoDescifradoAtbash")

    println("\n=== Prueba con diferentes alfabetos ===")
    val configCompleto = Config(Alphabet.COMPLETO)
    val vigenereCompleto = Vigenere(configCompleto)

    val textoMixto = "Hello 123!"
    val textoCifradoMixto = vigenereCompleto.cifrar(textoMixto, "key")
    val textoDescifradoMixto = vigenereCompleto.descifrar(textoCifradoMixto, "key")

    println("Texto mixto: $textoMixto")
    println("Texto cifrado: $textoCifradoMixto")
    println("Texto descifrado: $textoDescifradoMixto")
}
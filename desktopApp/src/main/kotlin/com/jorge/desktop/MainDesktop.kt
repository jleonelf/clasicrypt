// Archivo: MainDesktop.kt
package com.jorge.desktop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.jorge.clasicrypt.*

/**
 * Función principal que inicia la aplicación de escritorio
 * Configura la ventana principal y aplica el tema Material Design
 */
@OptIn(ExperimentalMaterial3Api::class)
fun main() = application {
    Window(
        onCloseRequest = ::exitApplication, // Cierra la aplicación al solicitar cierre
        title = "Cifrados Clásicos - Vigenère & Atbash"
    ) {
        MaterialTheme {
            PantallaPrincipal() // Muestra la pantalla principal
        }
    }
}

/**
 * Pantalla principal de la aplicación
 * Contiene todos los controles de interfaz y gestiona el estado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PantallaPrincipal() {
    // ========== DECLARACIÓN DE ESTADOS ==========

    // Estado del algoritmo seleccionado
    var algoritmo by remember { mutableStateOf("Vigenère") }
    val algoritmos = listOf("Vigenère", "Atbash")

    // Estado del alfabeto seleccionado
    var alfabetoSeleccionado by remember { mutableStateOf("Español") }

    // Estados del texto de entrada y salida
    var textoEntrada by remember { mutableStateOf("HOLA MUNDO") }
    var textoSalida by remember { mutableStateOf("") }

    // Estado de la clave (solo para Vigenère)
    var clave by remember { mutableStateOf("CLAVE") }

    // Estados de configuración
    var preservarCase by remember { mutableStateOf(true) }
    var ignorarDesconocidos by remember { mutableStateOf(true) }

    // ========== CONFIGURACIÓN DEL SISTEMA ==========

    // Configuración reactiva que se actualiza automáticamente
    val configuracion = remember(alfabetoSeleccionado, preservarCase, ignorarDesconocidos) {
        val alfabeto = Alphabet.desdeNombre(alfabetoSeleccionado)
        Config(
            alfabeto = alfabeto,
            preservarMayusculas = preservarCase,
            ignorarDesconocidos = ignorarDesconocidos
        )
    }

    // Instancia del cifrador Vigenère (memorizada para rendimiento)
    val cifradorVigenere by remember(configuracion) {
        mutableStateOf(Vigenere(configuracion))
    }

    // ========== INTERFAZ DE USUARIO ==========

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Título de la aplicación
        Text("Cifrados Clásicos", style = MaterialTheme.typography.headlineSmall)

        // ========== SELECTORES PRINCIPALES ==========

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Selector de algoritmo
            SelectorDesplegable(
                etiqueta = "Algoritmo",
                valorActual = algoritmo,
                opciones = algoritmos,
                onCambio = { algoritmo = it }
            )

            // Selector de alfabeto
            SelectorDesplegable(
                etiqueta = "Alfabeto",
                valorActual = alfabetoSeleccionado,
                opciones = Alphabet.nombresDisponibles(),
                onCambio = { alfabetoSeleccionado = it }
            )
        }

        // ========== OPCIONES DE CONFIGURACIÓN ==========

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            ChipConfiguracion(
                "Preservar mayúsc./minúsc.",
                preservarCase
            ) { preservarCase = it }

            ChipConfiguracion(
                "Ignorar no alfabéticos",
                ignorarDesconocidos
            ) { ignorarDesconocidos = it }
        }

        // ========== CAMPO DE CLAVE (SOLO VIGENÈRE) ==========

        if (algoritmo == "Vigenère") {
            OutlinedTextField(
                value = clave,
                onValueChange = { clave = it },
                label = { Text("Clave") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // ========== ÁREA DE TEXTO DE ENTRADA ==========

        OutlinedTextField(
            value = textoEntrada,
            onValueChange = { textoEntrada = it },
            label = { Text("Entrada") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )

        // ========== BOTONES DE ACCIÓN ==========

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Verifica si los parámetros son válidos para ejecutar
            val parametrosListos = when (algoritmo) {
                "Vigenère" -> clave.isNotBlank() // Vigenère requiere clave
                "Atbash" -> true // Atbash siempre está listo
                else -> true
            }

            // Botón para cifrar
            Button(
                enabled = parametrosListos,
                onClick = {
                    textoSalida = when (algoritmo) {
                        "Vigenère" -> cifradorVigenere.cifrar(textoEntrada, clave)
                        "Atbash" -> Atbash.cifrar(textoEntrada, configuracion)
                        else -> "Error: Algoritmo no soportado"
                    }
                }
            ) { Text("Cifrar") }

            // Botón para descifrar
            Button(
                enabled = parametrosListos,
                onClick = {
                    textoSalida = when (algoritmo) {
                        "Vigenère" -> cifradorVigenere.descifrar(textoEntrada, clave)
                        "Atbash" -> Atbash.descifrar(textoEntrada, configuracion)
                        else -> "Error: Algoritmo no soportado"
                    }
                }
            ) { Text("Descifrar") }

            // Botón para intercambiar textos
            OutlinedButton(
                onClick = {
                    val temporal = textoEntrada
                    textoEntrada = textoSalida
                    textoSalida = temporal
                }
            ) { Text("Intercambiar") }

            // Botón para limpiar campos
            TextButton(
                onClick = {
                    textoEntrada = ""
                    textoSalida = ""
                }
            ) { Text("Limpiar") }
        }

        // ========== ÁREA DE TEXTO DE SALIDA ==========

        OutlinedTextField(
            value = textoSalida,
            onValueChange = { textoSalida = it },
            label = { Text("Salida") },
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 120.dp)
        )

        // ========== INFORMACIÓN CONTEXTUAL ==========

        TextoAyuda(algoritmo)
    }
}

// ========== COMPONENTES REUTILIZABLES ==========

/**
 * Componente de selector desplegable personalizado
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SelectorDesplegable(
    etiqueta: String,
    valorActual: String,
    opciones: List<String>,
    onCambio: (String) -> Unit,
    enabled: Boolean = true
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { if (enabled) expanded = !expanded }
    ) {
        OutlinedTextField(
            value = valorActual,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(etiqueta) },
            modifier = Modifier.menuAnchor().width(240.dp),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            }
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            opciones.forEach { opcion ->
                DropdownMenuItem(
                    text = { Text(opcion) },
                    onClick = {
                        onCambio(opcion)
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Chip que funciona como interruptor de configuración
 */
@Composable
private fun ChipConfiguracion(
    texto: String,
    checked: Boolean,
    onChange: (Boolean) -> Unit
) {
    FilterChip(
        selected = checked,
        onClick = { onChange(!checked) },
        label = { Text(texto) }
    )
}

/**
 * Muestra texto de ayuda contextual según el algoritmo
 */
@Composable
private fun TextoAyuda(algoritmo: String) {
    val texto = when (algoritmo) {
        "Vigenère" ->
            "Cifrado polialfabético que usa una clave repetida cíclicamente. " +
                    "Cada letra se desplaza según la letra correspondiente de la clave."

        "Atbash" ->
            "Cifrado de sustitución monoalfabético que invierte el alfabeto " +
                    "(A↔Z, B↔Y, etc.). El cifrado y descifrado son la misma operación."

        else -> ""
    }

    if (texto.isNotBlank()) {
        Text(
            texto,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
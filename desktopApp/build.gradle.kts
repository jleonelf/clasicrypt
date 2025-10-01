import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    // NO pongas versión aquí; la toma de settings.gradle.kts (2.1.21 en tu captura)
    kotlin("jvm")
    // Usa Compose 1.7.0 (compatible con Kotlin 2.1.21)
    id("org.jetbrains.compose") version "1.7.0"
    // Requerido desde Kotlin 2.x
    id("org.jetbrains.kotlin.plugin.compose") version "2.1.21"
}

repositories {
    mavenCentral()
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(project(":")) // reutiliza tu librería del módulo raíz
}

kotlin {
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "com.jorge.desktop.MainDesktopKt"
        nativeDistributions {
            // En Windows: usa solo MSI (puedes agregar Deb si compilas en Linux)
            targetFormats(TargetFormat.Msi)
            packageName = "ClasiCrypt"
            packageVersion = "1.0.0"
        }
    }
}

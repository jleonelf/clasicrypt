plugins {
    kotlin("jvm") version "2.0.0"
    application
}

group = "com.jorge"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test")) // JUnit 5 via kotlin-test
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

// ⚠️ Ajusta este FQCN según el package real de tu Main.kt
application {
    mainClass.set("com.jorge.MainKt")
}

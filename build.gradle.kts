group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.61"
}

repositories {
    mavenLocal()
}

kotlin {
    linuxX64 {
        compilations.getByName("main") {
            dependencies {
                val guiVistaVer = "0.1-SNAPSHOT"
                implementation("org.guivista:guivista-gui-linuxx64:$guiVistaVer")
                implementation("org.guivista:guivista-io-linuxx64:$guiVistaVer")
            }
        }
        binaries {
            executable("image_show") {
                entryPoint = "org.example.image_show.main"
            }
        }

        sourceSets {
            @Suppress("UNUSED_VARIABLE") val linuxX64Main by getting {
                languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            }
        }
    }
}
group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.4.21"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/guivista/public")
    }
}

kotlin {
    linuxX64 {
        compilations.getByName("main") {
            dependencies {
                val guiVistaGuiVer = "0.3.3"
                cinterops.create("glib2")
                cinterops.create("gio2")
                cinterops.create("gtk3")
                implementation("org.guivista:guivista-gui:$guiVistaGuiVer")
            }
        }
        binaries {
            executable("image_show") {
                entryPoint = "org.example.image_show.main"
            }
        }

        sourceSets {
            @Suppress("UNUSED_VARIABLE")
            val linuxX64Main by getting {
                languageSettings.useExperimentalAnnotation("kotlin.ExperimentalUnsignedTypes")
            }
        }
    }
}
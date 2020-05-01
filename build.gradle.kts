group = "org.example"
version = "1.0-SNAPSHOT"

plugins {
    kotlin("multiplatform") version "1.3.72"
}

repositories {
    jcenter()
    mavenCentral()
    maven {
        val guiVistaCore = "16245519"
        url = uri("https://gitlab.com/api/v4/projects/$guiVistaCore/packages/maven")
    }
    maven {
        val guiVistaIo = "16243425"
        url = uri("https://gitlab.com/api/v4/projects/$guiVistaIo/packages/maven")
    }
    maven {
        val guiVistaGui = "15889948"
        url = uri("https://gitlab.com/api/v4/projects/$guiVistaGui/packages/maven")
    }
}

kotlin {
    linuxX64 {
        compilations.getByName("main") {
            dependencies {
                val guiVistaVer = "0.1.1"
                cinterops.create("glib2")
                cinterops.create("gio2")
                cinterops.create("gtk3")
                implementation("org.guivista:guivista-gui:$guiVistaVer")
                implementation("org.guivista:guivista-io:$guiVistaVer")
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
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
                val guiVistaCoreVer = "0.1-SNAPSHOT"
                implementation("org.guivista:guivista-core-linuxx64:$guiVistaCoreVer")
            }
        }
        binaries {
            executable("image_show") {
                entryPoint = "org.example.image_show.main"
            }
        }
    }
}
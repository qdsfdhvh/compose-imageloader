import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = Versions.Java.jvmTarget
        }
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val jvmMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(projects.extension.imageio)
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.seiko.imageloader.demo.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "Compose ImageLoader"
            packageVersion = "1.0.0"
            modules("java.sql") // https://github.com/JetBrains/compose-jb/issues/381
            modules("jdk.unsupported")
            modules("jdk.unsupported.desktop")
            macOS {
                bundleID = "com.seiko.imageloader.demo"
                // iconFile.set(project.file("src/jvmMain/resources/icon/ic_launcher.icns"))
            }
            linux {
                // iconFile.set(project.file("src/jvmMain/resources/icon/ic_launcher.png"))
            }
            windows {
                shortcut = true
                menu = true
                // iconFile.set(project.file("src/jvmMain/resources/icon/ic_launcher.ico"))
            }
        }
    }
}

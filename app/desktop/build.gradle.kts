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
        // error: Both artifact maps contains same key: .../desktop-jvm-1.2.0.jar with different values for kmp: [:app:desktop:jvmMain, :app:desktop:commonMain] and platform: :app:desktop:main
        // withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(projects.extension.imageio)
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
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

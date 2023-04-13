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
        val jvmMain by getting {
            kotlin.srcDir("src/jvmMain/jwm")
            dependencies {
                implementation(projects.app.common)
                implementation(compose.desktop.currentOs)
                implementation("io.github.humbleui:jwm:0.4.15")
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
            macOS {
                bundleID = "com.seiko.imageloader.demo2"
            }
            linux {
            }
            windows {
                shortcut = true
                menu = true
            }
        }
        buildTypes {
            release {
                proguard {
                    configurationFiles.from("proguard-rules.pro")
                }
            }
        }
    }
}

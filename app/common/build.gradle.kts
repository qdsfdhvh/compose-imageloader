import org.jetbrains.compose.compose
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose").version(Versions.compose_jb)
    id("com.android.library")
}

kotlin {
    android()
    jvm()
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.foundation)
                implementation(compose.material)
                api(projects.imageLoader)
                api("io.github.aakira:napier:2.6.1")
            }
        }
        val androidMain by getting
        val jvmMain by sourceSets.getting
    }
}

android {
    compileSdk = Versions.Android.compile
    buildToolsVersion = Versions.Android.buildTools
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Android.min
        targetSdk = Versions.Android.target
    }
    compileOptions {
        sourceCompatibility = Versions.Java.java
        targetCompatibility = Versions.Java.java
    }
}

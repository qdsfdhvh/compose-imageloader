import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization").version(Versions.Kotlin.lang)
    id("org.jetbrains.compose")
    id("com.android.library")
    // task error: Cannot change attributes of dependency configuration ':app:common:iosArm64ApiElements' after it has been resolved
    // id("dev.icerock.mobile.multiplatform-resources").version(Versions.multiplatformResources)
}

kotlin {
    android()
    jvm()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.ui)
                api(compose.foundation)
                api(compose.material)
                api(compose.runtime)

                api(projects.imageLoader)
                api("io.github.aakira:napier:${Versions.napier}")
                api("dev.icerock.moko:resources:${Versions.multiplatformResources}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlin.serialization}")
            }
        }
        val androidMain by getting
        val jvmMain by getting
        val appleMain by creating {
            dependsOn(commonMain)
        }
        val iosX64Main by getting {
            dependsOn(appleMain)
        }
        val iosArm64Main by getting {
            dependsOn(appleMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(appleMain)
        }
        val macosX64Main by getting {
            dependsOn(appleMain)
        }
        val macosArm64Main by getting {
            dependsOn(appleMain)
        }
        val jsMain by getting
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

// multiplatformResources {
//     multiplatformResourcesPackage = "com.seiko.imageloader.demo"
// }

// skip task because it's failed on gradle 7 and we not use results of this processing
// tasks.getByName("iosArm64ProcessResources").enabled = false

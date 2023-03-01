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
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = Versions.Java.jvmTarget
        }
    }
    ios()
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
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                api(compose.materialIconsExtended)

                api(projects.imageLoader)
                api(projects.extension.blur)

                api("dev.icerock.moko:resources:${Versions.multiplatformResources}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.Kotlin.serialization}")
                implementation("io.ktor:ktor-client-logging:${Versions.ktor}")
                implementation("io.github.aakira:napier:2.6.1")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
            }
        }
        val appleMain by creating {
            dependencies {
                implementation("io.ktor:ktor-client-darwin:${Versions.ktor}")
            }
        }
        val iosMain by getting {
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
        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:${Versions.ktor}")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.demo.common"
    compileSdk = Versions.Android.compile
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Android.min
    }
    compileOptions {
        sourceCompatibility = Versions.Java.source
        targetCompatibility = Versions.Java.target
    }
}

// multiplatformResources {
//     multiplatformResourcesPackage = "com.seiko.imageloader.demo"
// }

// skip task because it's failed on gradle 7 and we not use results of this processing
// tasks.getByName("iosArm64ProcessResources").enabled = false

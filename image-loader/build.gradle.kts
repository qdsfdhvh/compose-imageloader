import com.vanniktech.maven.publish.KotlinMultiplatform
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    android {
        publishLibraryVariants("debug", "release")
    }
    jvm("desktop")
    ios()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    js(IR) {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/commonMain/compose")
            dependencies {
                api(compose.ui)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}")
                api("com.squareup.okio:okio:${Versions.okio}")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                api("com.eygraber:uri-kmp:0.0.9")
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/gif")
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}")
                implementation("androidx.compose.ui:ui-graphics:1.4.0-alpha03")
                implementation("androidx.core:core-ktx:1.9.0")
                implementation("androidx.exifinterface:exifinterface:1.3.5")
                implementation("androidx.appcompat:appcompat-resources:1.5.1")
                implementation("com.google.accompanist:accompanist-drawablepainter:0.25.1")
                // svg
                implementation("com.caverock:androidsvg-aar:1.4")
            }
        }
        val skiaMain by creating {
            dependsOn(commonMain)
        }
        val desktopMain by getting {
            dependsOn(skiaMain)
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${Versions.Kotlin.coroutines}")
            }
        }
        val darwinMain by creating {
            dependsOn(skiaMain)
            dependencies {
                implementation("io.ktor:ktor-client-darwin:${Versions.ktor}")
            }
        }
        val iosMain by getting {
            dependsOn(darwinMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
        val macosX64Main by getting {
            dependsOn(darwinMain)
        }
        val macosArm64Main by getting {
            dependsOn(darwinMain)
        }
        val jsMain by getting {
            dependsOn(skiaMain)
            dependencies {
                implementation("io.ktor:ktor-client-js:${Versions.ktor}")
                implementation("com.squareup.okio:okio-nodefilesystem:${Versions.okio}")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
    compileSdk = Versions.Android.compile
    buildToolsVersion = Versions.Android.buildTools
    defaultConfig {
        minSdk = Versions.Android.min
        targetSdk = Versions.Android.target
    }
    compileOptions {
        sourceCompatibility = Versions.Java.java
        targetCompatibility = Versions.Java.java
    }
}

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinMultiplatform())
}

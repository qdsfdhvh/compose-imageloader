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
    macosX64()
    macosArm64()
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/commonMain/compose")
            dependencies {
                api(compose.foundation)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}")
                api("com.squareup.okio:okio:3.2.0")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.github.aakira:napier:2.6.1")
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/gif")
            dependencies {
                implementation("io.ktor:ktor-client-okhttp:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}")
                implementation("androidx.core:core-ktx:1.8.0")
                implementation("androidx.compose.ui:ui-graphics:${Versions.compose}")
                implementation("androidx.exifinterface:exifinterface:1.3.3")
                implementation("androidx.appcompat:appcompat-resources:1.4.2")
                implementation("com.google.accompanist:accompanist-drawablepainter:0.23.1")
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
        val macosX64Main by getting {
            dependsOn(darwinMain)
        }
        val macosArm64Main by getting {
            dependsOn(darwinMain)
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
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

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinMultiplatform())
}

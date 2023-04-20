import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("project-kmp")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/commonMain/singleton")
            dependencies {
                api(compose.ui)
                api(libs.kotlinx.coroutines.core)
                api(libs.okio)
                api(libs.ktor.core)
                api(libs.uri.kmp)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/singleton")
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
                implementation("androidx.core:core-ktx:1.10.0")
                implementation("androidx.exifinterface:exifinterface:1.3.6")
                implementation("androidx.appcompat:appcompat-resources:1.6.1")
                implementation("com.google.accompanist:accompanist-drawablepainter:0.28.0")
                // svg
                implementation("com.caverock:androidsvg-aar:1.4")
            }
        }
        val desktopMain by getting {
            kotlin.srcDir("src/desktopMain/singleton")
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        val darwinMain by getting {
            kotlin.srcDir("src/darwinMain/singleton")
        }
        val appleMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(libs.okio.fakefilesystem)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
}

mavenPublishing {
    @Suppress("UnstableApiUsage")
    configure(KotlinMultiplatform())
}

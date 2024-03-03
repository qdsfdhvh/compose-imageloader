plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                api(libs.ktor.client.core)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        appleMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        jsMain {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.wasmJs)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.ktor.network"
}

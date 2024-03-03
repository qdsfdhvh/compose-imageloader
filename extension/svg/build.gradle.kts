import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.androidsvg)
            }
        }
        val wasmJsMain by getting {
            dependsOn(skiaMain.get())
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.svg"
}

applyKtorWasmWorkaround(libs.versions.ktor.wasm.get())

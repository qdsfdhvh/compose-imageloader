import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()
    sourceSets {
        commonMain {
            dependencies {
                api(projects.imageLoader)
                api(projects.extension.ktorNetwork)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.bundles.test.common)
            }
        }
        val wasmJsMain by getting {
            dependsOn(darwinMain.get())
        }
        val noAndroidMain by creating {
            dependsOn(commonMain.get())
            desktopMain.get().dependsOn(this)
            appleMain.get().dependsOn(this)
            jsMain.get().dependsOn(this)
            wasmJsMain.dependsOn(this)
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
}

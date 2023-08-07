import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    @Suppress("OPT_IN_USAGE")
    wasm {
        moduleName = "compose-imageloader-demo"
        browser {
            commonWebpackConfig {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    open = mapOf(
                        "app" to mapOf(
                            "name" to "google chrome",
                        ),
                    ),
                )
            }
        }
        binaries.executable()
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val wasmMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.runtime)
                implementation(compose.ui)
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

// // See https://youtrack.jetbrains.com/issue/KT-57203 related issue
// tasks.named("wasmDevelopmentExecutableCompileSync") {
//    dependsOn("wasmBrowserProductionWebpack")
// }

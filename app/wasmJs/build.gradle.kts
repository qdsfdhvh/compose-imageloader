import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
}

kotlin {
    wasmJs {
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
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                implementation(compose.runtime)
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

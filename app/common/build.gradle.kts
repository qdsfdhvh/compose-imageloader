import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api(projects.imageLoaderSingleton)
                implementation(projects.extension.blur)
                implementation(projects.extension.composeResources)
                // implementation(projects.extension.mokoResources)
                implementation(projects.extension.ninePatch)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kermit)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.ktor.client.cio)
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
    }
    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "common"
            isStatic = true
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.demo.common"
}

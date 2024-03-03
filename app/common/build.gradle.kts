import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
}

kotlin {
    sourceSets {
        all {
            languageSettings {
                optIn("org.jetbrains.compose.resources.ExperimentalResourceApi")
            }
        }
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                api(projects.imageLoaderSingleton)
                implementation(projects.extension.composeResources)
                // implementation(projects.extension.mokoResources)
                implementation(projects.extension.ninePatch)

                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.serialization.kotlinx.json)
                implementation(libs.kermit)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        desktopMain {
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
        val filePickerMain by creating {
            dependsOn(commonMain.get())
            jvmMain.get().dependsOn(this)
            iosMain.get().dependsOn(this)
            jsMain.get().dependsOn(this)
            dependencies {
                implementation(libs.file.picker)
            }
        }
        val noFilePickerMain by creating {
            dependsOn(commonMain.get())
            macosMain.get().dependsOn(this)
            wasmJsMain.get().dependsOn(this)
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

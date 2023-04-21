import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

class ProjectKmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("com.android.library")
            }
            kotlin {
                @OptIn(ExperimentalKotlinGradlePluginApi::class)
                targetHierarchy.default {
                    group("jvm") {
                        withAndroid()
                        withJvm()
                    }
                    group("skia") {
                        withJvm()
                        group("darwin") {
                            withNative()
                            withJs()
                        }
                    }
                }
                android {
                    publishLibraryVariants("debug", "release")
                }
                jvm("desktop") {
                    compilations.all {
                        kotlinOptions.jvmTarget = Versions.Java.jvmTarget
                    }
                }
                ios()
                iosSimulatorArm64()
                macosX64()
                macosArm64()
                js(IR) {
                    browser()
                    nodejs()
                }
            }
            android {
                compileSdk = Versions.Android.compile
                defaultConfig {
                    minSdk = Versions.Android.min
                }
                compileOptions {
                    sourceCompatibility = Versions.Java.source
                    targetCompatibility = Versions.Java.target
                }
            }
        }
    }
}

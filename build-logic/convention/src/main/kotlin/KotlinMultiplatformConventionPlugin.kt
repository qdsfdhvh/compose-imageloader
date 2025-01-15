import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }
        extensions.configure<KotlinMultiplatformExtension> {
            androidTarget {
                publishLibraryVariants("release")
            }
            jvm("desktop")
            iosX64()
            iosArm64()
            iosSimulatorArm64()
            macosX64()
            macosArm64()
            js {
                browser()
            }
            @Suppress("OPT_IN_USAGE")
            wasmJs {
                browser {
                    // TODO: Fix wasm tests.
                    testTask {
                        enabled = false
                    }
                }
            }
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            applyHierarchyTemplate {
                common {
                    group("commonJvm") {
                        withAndroidTarget()
                        withJvm()
                    }
                    group("skia") {
                        withJvm()
                        group("darwin") {
                            group("apple") {
                                group("ios") {
                                    withIosX64()
                                    withIosArm64()
                                    withIosSimulatorArm64()
                                }
                                group("macos") {
                                    withMacosX64()
                                    withMacosArm64()
                                }
                            }
                            group("commonJs") {
                                withJs()
                                withWasmJs()
                            }
                        }
                    }
                }
            }
            targets.configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            // https://youtrack.jetbrains.com/issue/KT-61573
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }
        }
        configKotlin()
    }
}

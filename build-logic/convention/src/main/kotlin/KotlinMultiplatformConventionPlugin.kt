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
                publishLibraryVariants("debug", "release")
            }
            jvm("desktop")
            iosX64()
            iosArm64()
            iosSimulatorArm64()
            macosX64()
            macosArm64()
            js(IR) {
                browser()
                nodejs()
            }
            @OptIn(ExperimentalKotlinGradlePluginApi::class)
            targetHierarchy.custom {
                common {
                    group("jvm") {
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
                            withJs()
                        }
                    }
                }
            }
        }
        configKotlin()
    }
}

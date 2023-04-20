import org.gradle.api.Plugin
import org.gradle.api.Project

class ProjectKmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("com.android.library")
            }
            kotlin {
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

                @Suppress("UNUSED_VARIABLE")
                sourceSets.apply {
                    val commonMain = getByName("commonMain")
                    val jvmMain = maybeCreate("jvmMain").apply {
                        dependsOn(commonMain)
                    }
                    val skiaMain = maybeCreate("skiaMain").apply {
                        dependsOn(commonMain)
                    }
                    val androidMain = getByName("androidMain").apply {
                        dependsOn(jvmMain)
                    }
                    val desktopMain = getByName("desktopMain").apply {
                        dependsOn(jvmMain)
                        dependsOn(skiaMain)
                    }
                    val darwinMain = maybeCreate("darwinMain").apply {
                        dependsOn(skiaMain)
                    }
                    val appleMain = maybeCreate("appleMain").apply {
                        dependsOn(darwinMain)
                    }
                    val iosMain = getByName("iosMain").apply {
                        dependsOn(appleMain)
                    }
                    val iosSimulatorArm64Main = getByName("iosSimulatorArm64Main").apply {
                        dependsOn(appleMain)
                    }
                    val macosX64Main = getByName("macosX64Main").apply {
                        dependsOn(appleMain)
                    }
                    val macosArm64Main = getByName("macosArm64Main").apply {
                        dependsOn(appleMain)
                    }
                    val jsMain = getByName("jsMain").apply {
                        dependsOn(darwinMain)
                    }
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
import org.gradle.api.Plugin
import org.gradle.api.Project

class ProjectKmpPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                // top android library https://stackoverflow.com/questions/64775638/kotlin-multiplatform-publish-android-library-failing
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
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
                    val commonTest = getByName("commonTest")
                    val jvmMain = maybeCreate("jvmMain").apply {
                        dependsOn(commonMain)
                    }
                    val jvmTest = maybeCreate("jvmTest").apply {
                        dependsOn(commonTest)
                    }
                    val skiaMain = maybeCreate("skiaMain").apply {
                        dependsOn(commonMain)
                    }
                    val skiaTest = maybeCreate("skiaTest").apply {
                        dependsOn(commonTest)
                    }
                    val androidMain = getByName("androidMain").apply {
                        dependsOn(jvmMain)
                    }
                    val androidUnitTest = getByName("androidUnitTest").apply {
                        dependsOn(jvmTest)
                    }
                    val desktopMain = getByName("desktopMain").apply {
                        dependsOn(jvmMain)
                        dependsOn(skiaMain)
                    }
                    val desktopTest = getByName("desktopTest").apply {
                        dependsOn(jvmTest)
                        dependsOn(skiaTest)
                    }
                    val darwinMain = maybeCreate("darwinMain").apply {
                        dependsOn(skiaMain)
                    }
                    val darwinTest = maybeCreate("darwinTest").apply {
                        dependsOn(skiaTest)
                    }
                    val appleMain = maybeCreate("appleMain").apply {
                        dependsOn(darwinMain)
                    }
                    val appleTest = maybeCreate("appleTest").apply {
                        dependsOn(darwinTest)
                    }
                    val iosMain = getByName("iosMain").apply {
                        dependsOn(appleMain)
                    }
                    val iosTest = getByName("iosTest").apply {
                        dependsOn(appleTest)
                    }
                    val iosSimulatorArm64Main = getByName("iosSimulatorArm64Main").apply {
                        dependsOn(iosMain)
                    }
                    val iosSimulatorArm64Test = getByName("iosSimulatorArm64Test").apply {
                        dependsOn(iosTest)
                    }
                    val macosMain = maybeCreate("macosMain").apply {
                        dependsOn(appleMain)
                    }
                    val macosTest = maybeCreate("macosTest").apply {
                        dependsOn(appleTest)
                    }
                    val macosX64Main = getByName("macosX64Main").apply {
                        dependsOn(macosMain)
                    }
                    val macosX64Test = getByName("macosX64Test").apply {
                        dependsOn(macosTest)
                    }
                    val macosArm64Main = getByName("macosArm64Main").apply {
                        dependsOn(macosMain)
                    }
                    val macosArm64Test = getByName("macosArm64Test").apply {
                        dependsOn(macosTest)
                    }
                    val jsMain = getByName("jsMain").apply {
                        dependsOn(darwinMain)
                    }
                    val jsTest = getByName("jsTest").apply {
                        dependsOn(darwinTest)
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

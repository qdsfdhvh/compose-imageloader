import org.gradle.api.Plugin
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

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
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                macosX64()
                macosArm64()
                js(IR) {
                    nodejs {
                        testTask {
                            useMocha {
                                timeout = "8000"
                            }
                        }
                    }
                    browser {
                        testTask {
                            testLogging.showStandardStreams = true
                            useKarma {
                                useChromeHeadless()
                                useFirefox()
                            }
                        }
                    }
                }
                @OptIn(ExperimentalWasmDsl::class)
                wasm {
                    d8()
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
                    val jsNativeMain = maybeCreate("jsNativeMain").apply {
                        dependsOn(skiaMain)
                    }
                    val appleMain = maybeCreate("appleMain").apply {
                        dependsOn(jsNativeMain)
                    }
                    val iosMain = maybeCreate("iosMain").apply {
                        dependsOn(appleMain)
                    }
                    val iosX64Main = getByName("iosX64Main").apply {
                        dependsOn(iosMain)
                    }
                    val iosArm64Main = getByName("iosArm64Main").apply {
                        dependsOn(iosMain)
                    }
                    val iosSimulatorArm64Main = getByName("iosSimulatorArm64Main").apply {
                        dependsOn(iosMain)
                    }
                    val macosMain = maybeCreate("macosMain").apply {
                        dependsOn(appleMain)
                    }
                    val macosX64Main = getByName("macosX64Main").apply {
                        dependsOn(macosMain)
                    }
                    val macosArm64Main = getByName("macosArm64Main").apply {
                        dependsOn(macosMain)
                    }
                    val jsWasmMain = maybeCreate("jsWasmMain").apply {
                        dependsOn(jsNativeMain)
                    }
                    val jsMain = getByName("jsMain").apply {
                        dependsOn(jsWasmMain)
                        dependencies {
                            implementation(kotlin("stdlib-js"))
                        }
                    }
                    val wasmMain = getByName("wasmMain").apply {
                        dependsOn(jsWasmMain)
                        dependencies {
                            implementation(kotlin("stdlib-wasm"))
                        }
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

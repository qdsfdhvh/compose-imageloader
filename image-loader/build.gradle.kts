import org.jetbrains.kotlin.config.LanguageFeature
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.baselineProfile)
    alias(libs.plugins.roborazzi)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs()
    sourceSets {
        all {
            languageSettings {
                optIn("kotlin.experimental.ExperimentalNativeApi")
            }
        }
        commonMain {
            dependencies {
                api(compose.foundation)
                api(compose.ui)
                api(libs.kotlinx.coroutines.core)
                api(libs.okio)
                api(libs.ktor.client.core)
                api(libs.uri.kmp)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.bundles.test.common)
                implementation(compose.foundation)
                implementation(compose.ui)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        androidMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.exifinterface)
                implementation(libs.androidsvg)
            }
        }
        androidUnitTest {
            dependencies {
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.bundles.test.android)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        desktopTest {
            languageSettings {
                enableLanguageFeature(LanguageFeature.ContextReceivers.name)
            }
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.roborazzi.compose.desktop.get().toString()) {
                    exclude("org.jetbrains.compose.ui", "ui-test-junit4-desktop")
                    exclude("org.jetbrains.compose.ui", "ui-graphics-desktop")
                }
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
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.client.wasmJs)
            }
        }
        val noJsAndWasmMain by creating {
            dependsOn(commonMain.get())
            jvmMain.get().dependsOn(this)
            appleMain.get().dependsOn(this)
            dependencies {
                implementation(libs.androidx.collection)
            }
        }
        val noAndroidMain by creating {
            dependsOn(commonMain.get())
            desktopMain.get().dependsOn(this)
            appleMain.get().dependsOn(this)
            jsMain.get().dependsOn(this)
            wasmJsMain.dependsOn(this)
        }
    }
    sourceSets.all {
        if (name.endsWith("Main")) {
            kotlin.srcDir("src/$name/singleton")
        }
    }
}

@Suppress("UnstableApiUsage")
android {
    namespace = "io.github.qdsfdhvh.imageloader"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

baselineProfile {
    baselineProfileOutputDir = "../../src/androidMain/generated/baselineProfiles"
    filter {
        include("com.seiko.imageloader.**")
        exclude("com.seiko.imageloader.demo.**")
        exclude("com.seiko.imageloader.util.Logger")
    }
}

dependencies {
    baselineProfile(projects.app.android.benchmark)
}

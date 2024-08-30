import com.github.takahirom.roborazzi.ExperimentalRoborazziApi
import org.jetbrains.kotlin.config.LanguageFeature

plugins {
    id("app.android.library")
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
    alias(libs.plugins.roborazzi)
}

kotlin {
    androidTarget()
    jvm("desktop")
    // iosArm64()
    applyDefaultHierarchyTemplate()
    sourceSets {
        all {
            languageSettings {
                optIn("androidx.compose.ui.test.ExperimentalTestApi")
                optIn("com.github.takahirom.roborazzi.ExperimentalRoborazziApi")
            }
        }
        commonMain {
            dependencies {
                implementation(projects.imageLoaderSingleton)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit)
                implementation(libs.androidx.test.junit)
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.robolectric)
                implementation(libs.roborazzi.core)
                implementation(libs.roborazzi.junit)
                implementation(libs.roborazzi.compose.android)

                // use to roborazzi plugin preview
                implementation(libs.roborazzi.compose.preview.scanner.support)
                implementation(libs.composable.preview.scanner)
            }
        }
        val desktopTest by getting {
            languageSettings {
                enableLanguageFeature(LanguageFeature.ContextReceivers.name)
            }
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.desktop.uiTestJUnit4)
                implementation(libs.roborazzi.compose.desktop)
            }
        }
        // iosTest {
        //     dependencies {
        //         implementation(libs.roborazzi.compose.ios)
        //     }
        // }
    }
    jvmToolchain(17)
}

android {
    namespace = "com.seiko.imageloader.test.roborazzi"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.systemProperties["robolectric.pixelCopyRenderMode"] = "hardware"
            }
        }
    }
}

roborazzi {
    @OptIn(ExperimentalRoborazziApi::class)
    generateComposePreviewRobolectricTests {
        enable = true
        packages = listOf("com.seiko.imageloader.test.roborazzi")
        robolectricConfig = mapOf(
            "sdk" to "[32]",
            "qualifiers" to "RobolectricDeviceQualifiers.Pixel5",
        )
        includePrivatePreviews = true
    }
}

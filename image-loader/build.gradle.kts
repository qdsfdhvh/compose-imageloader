plugins {
    id("project-kmp")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.baselineProfile)
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/commonMain/singleton")
            dependencies {
                api(compose.ui)
                api(libs.kotlinx.coroutines.core)
                api(libs.okio)
                api(libs.ktor.client.core)
                api(libs.uri.kmp)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/singleton")
            dependencies {
                implementation(libs.kotlinx.coroutines.android)
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.appcompat)
                implementation(libs.androidx.exifinterface)
                implementation(libs.accompanist.drawablepainter)
                implementation(libs.androidsvg)
            }
        }
        val desktopMain by getting {
            kotlin.srcDir("src/desktopMain/singleton")
            dependencies {
                implementation(libs.kotlinx.coroutines.swing)
            }
        }
        val darwinMain by getting {
            kotlin.srcDir("src/darwinMain/singleton")
        }
        val appleMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
        val noJsMain by creating {
            dependsOn(commonMain)
            jvmMain.dependsOn(this)
            appleMain.dependsOn(this)
            dependencies {
                implementation(libs.androidx.collection)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
}

baselineProfile {
    baselineProfileOutputDir = "../../src/androidMain/generated/baselineProfiles"
    filter {
        include("com.seiko.imageloader.**")
    }
}

dependencies {
    baselineProfile(projects.app.android.benchmark)
}

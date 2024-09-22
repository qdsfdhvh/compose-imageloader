plugins {
    id("app.android.library")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
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
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                implementation("dev.icerock.moko:resources:0.24.3")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.moko.resources"
}

configKotlin()

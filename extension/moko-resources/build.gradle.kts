plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                implementation("dev.icerock.moko:resources:0.23.0")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.moko.resources"
}

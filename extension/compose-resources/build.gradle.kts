plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.compose.resources"
}

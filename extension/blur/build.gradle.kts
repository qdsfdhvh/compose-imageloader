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
            }
        }
        androidMain {
            dependencies {
                implementation("com.github.android:renderscript-intrinsics-replacement-toolkit:9a70eae6f1")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.blur"
}

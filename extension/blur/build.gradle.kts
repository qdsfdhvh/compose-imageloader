plugins {
    id("project-kmp")
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
        @Suppress("UNUSED_VARIABLE")
        val androidMain by getting {
            dependencies {
                implementation("com.github.android:renderscript-intrinsics-replacement-toolkit:9a70eae6f1")
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.blur"
    compileSdk = Versions.Android.compile
    defaultConfig {
        minSdk = Versions.Android.min
    }
    compileOptions {
        sourceCompatibility = Versions.Java.source
        targetCompatibility = Versions.Java.target
    }
}

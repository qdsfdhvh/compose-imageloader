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
                implementation(libs.moko.resources)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.moko.resources"
    compileSdk = Versions.Android.compile
    defaultConfig {
        minSdk = Versions.Android.min
    }
    compileOptions {
        sourceCompatibility = Versions.Java.source
        targetCompatibility = Versions.Java.target
    }
}

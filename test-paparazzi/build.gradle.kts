plugins {
    id("app.android.library")
    id("app.kotlin.android")
    id("app.compose.multiplatform")
    id("app.cash.paparazzi") version "1.3.5"
}

kotlin {
    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(21))
        }
    }
}

android {
    namespace = "com.seiko.imageloader.test.paparazzi"
}

dependencies {
    testImplementation(libs.kotlinx.coroutines.test)
}

dependencies {
    implementation(projects.imageLoaderSingleton)
}

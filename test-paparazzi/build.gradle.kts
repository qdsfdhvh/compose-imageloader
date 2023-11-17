plugins {
    id("app.android.library")
    id("app.kotlin.android")
    id("app.compose.multiplatform")
    id("app.cash.paparazzi") version "1.3.1"
}

android {
    namespace = "com.seiko.imageloader.test.paparazzi"
}

dependencies {
    implementation(projects.imageLoader)
}

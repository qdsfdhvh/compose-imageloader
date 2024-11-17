plugins {
    id("app.android.library")
    id("app.kotlin.android")
    id("app.compose.multiplatform")
    id("app.cash.paparazzi") version "1.3.5"
}

android {
    namespace = "com.seiko.imageloader.test.paparazzi"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(libs.kotlinx.coroutines.test)
}

dependencies {
    implementation(projects.imageLoaderSingleton)
}

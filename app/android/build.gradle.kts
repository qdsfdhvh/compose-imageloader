plugins {
    id("app.android.application")
    id("app.kotlin.android")
    id("app.compose.multiplatform")
    alias(libs.plugins.baselineProfile)
}

android {
    namespace = "com.seiko.imageloader.demo"
    defaultConfig {
        applicationId = "com.seiko.imageloader.demo"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
        create("benchmark") {
            initWith(buildTypes.getByName("release"))
            signingConfig = signingConfigs.getByName("debug")
            matchingFallbacks += listOf("release")
            isDebuggable = false
        }
    }
}

baselineProfile {
    filter {
        include("com.seiko.imageloader.demo.**")
    }
}

dependencies {
    implementation(projects.app.common)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
}

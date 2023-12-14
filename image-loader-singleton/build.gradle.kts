plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.baselineProfile)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.imageLoader)
                api(projects.extension.ktorNetwork)
            }
        }
        val noAndroidMain by creating {
            dependsOn(commonMain.get())
            desktopMain.get().dependsOn(this)
            appleMain.get().dependsOn(this)
            jsMain.get().dependsOn(this)
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
}

baselineProfile {
    baselineProfileOutputDir = "../../src/androidMain/generated/baselineProfiles"
    filter {
        include("com.seiko.imageloader.**")
    }
}

dependencies {
    baselineProfile(projects.app.android.benchmark)
}

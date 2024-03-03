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
                api(projects.imageLoader)
                api(projects.extension.ktorNetwork)
                api(projects.extension.svg)
            }
        }
        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(libs.bundles.test.common)
            }
        }
        val noAndroidMain by creating {
            dependsOn(commonMain.get())
            desktopMain.get().dependsOn(this)
            appleMain.get().dependsOn(this)
            jsMain.get().dependsOn(this)
            wasmJsMain.get().dependsOn(this)
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader"
}

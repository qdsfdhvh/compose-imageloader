plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        @Suppress("UNUSED_VARIABLE")
        val jsMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.runtime)
                implementation(libs.okio.fakefilesystem)
                implementation(npm("path-browserify", "^1.0.1"))
                implementation(npm("os-browserify", "^0.3.0"))
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

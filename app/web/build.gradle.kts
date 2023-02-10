

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser()
        // Causes build to take too long, turn it off temporarily
        // binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.web.core)
                implementation(compose.runtime)
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

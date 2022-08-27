import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

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
        val jsMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(npm("path-browserify", "^1.0.1"))
                implementation(npm("os-browserify", "^0.3.0"))

                implementation(npm("fs-extra", "9.1.0"))
                implementation(npm("assert", "^2.0.0"))
                implementation(npm("stream-browserify", "^3.0.0"))
                implementation(npm("constants-browserify", "^1.0.0"))
                implementation(npm("buffer", "^6.0.3"))
                implementation(devNpm("process", "^0.11.10"))
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

// a temporary workaround for a bug in jsRun invocation - see https://youtrack.jetbrains.com/issue/KT-48273
afterEvaluate {
    rootProject.extensions.configure<NodeJsRootExtension> {
        versions.webpack.version = "5.74.0"
        versions.webpackDevServer.version = "4.10.0"
        versions.webpackCli.version = "4.10.0"
        // nodeVersion = "16.0.0"
    }
}

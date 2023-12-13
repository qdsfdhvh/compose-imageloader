plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        jsMain {
            // TODO remove if plugin support dependency resources dir
            resources.srcDir("../common/src/commonMain/resources")
            dependencies {
                implementation(projects.app.common)
                implementation(compose.runtime)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.okio.fakefilesystem)
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
    alias(libs.plugins.moko.resources)
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.runtime)
                implementation(libs.okio.fakefilesystem)
            }
        }
    }
}

compose.experimental {
    web.application {
    }
}

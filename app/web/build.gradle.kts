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
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.app.common)
                implementation(compose.runtime)
                implementation(libs.okio.fakefilesystem)
                implementation(libs.moko.resources)
            }
        }
        val jsMain by getting {
            // https://github.com/icerockdev/moko-resources/issues/531
            dependsOn(commonMain)
        }
    }
}

compose.experimental {
    web.application {
    }
}

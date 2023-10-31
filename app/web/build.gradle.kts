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
                implementation(libs.moko.resources)
            }
        }
        getByName("jsMain") {
            // https://github.com/icerockdev/moko-resources/issues/531
            dependsOn(commonMain.get())
        }
    }
}

compose.experimental {
    web.application {
    }
}

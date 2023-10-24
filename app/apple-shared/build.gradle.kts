plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
    alias(libs.plugins.moko.resources)
    kotlin("native.cocoapods")
}

kotlin {
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.common)
            }
        }
    }
    cocoapods {
        name = "shared"
        version = "1.0.0"
        summary = "Shared code for the sample"
        homepage = "https://github.com/qdsfdhvh/compose-imageloader"
        ios.deploymentTarget = "14.0"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
    }
}

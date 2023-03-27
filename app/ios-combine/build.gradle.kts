plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("org.jetbrains.compose")
}

kotlin {
    ios()
    iosSimulatorArm64()
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.app.common)
            }
        }
    }
    cocoapods {
        name = "combine"
        version = "1.0.0"
        summary = "Shared code for the sample"
        homepage = "https://github.com/qdsfdhvh/compose-imageloader"
        ios.deploymentTarget = "16.0"
        podfile = project.file("../ios/Podfile")
        framework {
            baseName = "combine"
            isStatic = true
        }
    }
}

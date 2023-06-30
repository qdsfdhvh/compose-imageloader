plugins {
    kotlin("multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = Versions.Java.jvmTarget
        }
    }
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                // svg
                implementation("com.twelvemonkeys.imageio:imageio-batik:3.9.4")
                implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
            }
        }
    }
}

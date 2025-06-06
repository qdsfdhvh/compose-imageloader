plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    jvm()
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                // svg
                implementation("com.twelvemonkeys.imageio:imageio-batik:3.12.0")
                implementation("org.apache.xmlgraphics:batik-transcoder:1.18")
            }
        }
    }
}

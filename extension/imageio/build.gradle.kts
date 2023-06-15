plugins {
    kotlin("jvm")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

dependencies {
    implementation(projects.imageLoader)
    // svg
    implementation("com.twelvemonkeys.imageio:imageio-batik:3.9.4")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
}

java {
    sourceCompatibility = Versions.Java.source
    targetCompatibility = Versions.Java.target
}

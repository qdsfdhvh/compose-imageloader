import com.vanniktech.maven.publish.KotlinJvm

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish.base")
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

@Suppress("UnstableApiUsage")
mavenPublishing {
    configure(KotlinJvm())
    pomFromGradleProperties()
}

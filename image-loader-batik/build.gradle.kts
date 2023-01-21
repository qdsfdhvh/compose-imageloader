import com.vanniktech.maven.publish.KotlinJvm
import com.vanniktech.maven.publish.MavenPublishBaseExtension

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish.base")
}

dependencies {
    api(projects.imageLoader)
    // svg
    implementation("com.twelvemonkeys.imageio:imageio-batik:3.8.3")
    implementation("org.apache.xmlgraphics:batik-transcoder:1.16")
}

java {
    sourceCompatibility = Versions.Java.source
    targetCompatibility = Versions.Java.target
}

@Suppress("UnstableApiUsage")
configure<MavenPublishBaseExtension> {
    configure(KotlinJvm())
}

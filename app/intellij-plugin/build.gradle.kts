plugins {
    id("org.jetbrains.intellij") version "1.15.0"
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    idea
}

dependencies {
    implementation(projects.app.common)
    implementation(compose.desktop.currentOs)
    testImplementation("junit", "junit", "4.13.2")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
}

java {
    sourceCompatibility = Versions.Java.source
    targetCompatibility = Versions.Java.target
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.1")
}

// fix ktor @see https://stackoverflow.com/questions/75694002/problem-with-ktor-client-in-intellij-idea-plugin-development
configurations.all {
    exclude("org.slf4j", "slf4j-api")
}

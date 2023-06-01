plugins {
    id("org.jetbrains.intellij") version "1.13.3"
    java
    kotlin("jvm")
    id("org.jetbrains.compose")
    idea
}

dependencies {
    implementation(projects.app.common)
    implementation(compose.desktop.currentOs)
    testImplementation("junit", "junit", "4.12")
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

java {
    sourceCompatibility = Versions.Java.source
    targetCompatibility = Versions.Java.target
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.1")
}

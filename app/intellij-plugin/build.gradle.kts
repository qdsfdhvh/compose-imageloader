plugins {
    id("org.jetbrains.intellij") version "1.16.1"
    java
    id("app.kotlin.jvm")
    id("app.compose.multiplatform")
    idea
}

dependencies {
    implementation(projects.app.common)
    implementation(compose.desktop.currentOs)
    testImplementation(libs.junit)
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
}

// See https://github.com/JetBrains/gradle-intellij-plugin/
intellij {
    version.set("2023.1")
}

// fix ktor @see https://stackoverflow.com/questions/75694002/problem-with-ktor-client-in-intellij-idea-plugin-development
configurations.all {
    exclude("org.slf4j", "slf4j-api")
}

import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("app.kotlin.jvm")
    id("app.compose.multiplatform")
    id("org.jetbrains.intellij.platform") version "2.2.1"
}

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

    intellijPlatform {
        defaultRepositories()
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3.1.1")
        bundledPlugins(
            "org.jetbrains.kotlin",
            "com.intellij.java",
        )
        zipSigner()
        testFramework(TestFrameworkType.Platform)
    }

    implementation(projects.app.common)
    implementation(compose.desktop.currentOs)

    testImplementation(libs.junit)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

// fix ktor @see https://stackoverflow.com/questions/75694002/problem-with-ktor-client-in-intellij-idea-plugin-development
configurations.all {
    exclude("org.slf4j", "slf4j-api")
}

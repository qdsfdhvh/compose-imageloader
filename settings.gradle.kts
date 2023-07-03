pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://repo1.maven.org/maven2")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    }
    includeBuild("build-logic")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenLocal()
        mavenCentral()
        maven("https://repo1.maven.org/maven2")
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/bootstrap")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.13.4"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        publishAlwaysIf(System.getenv("GITHUB_ACTIONS") != null)
    }
}

rootProject.name = "compose-imageLoader"

include(
    ":image-loader",
    ":extension:blur",
    // ":extension:imageio",
    // ":extension:moko-resources",
    ":app:common",
    ":app:android",
    ":app:android:benchmark",
    ":app:desktop",
    ":app:ios-combine",
    ":app:macos",
    ":app:wasm",
    ":app:web",
    ":app:intellij-plugin",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

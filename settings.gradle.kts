pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        maven("https://repo1.maven.org/maven2")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("build-logic")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // error with js
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        maven("https://repo1.maven.org/maven2")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://jitpack.io")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.13.3"
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
    ":extension:imageio",
    ":app:common",
    ":app:android",
    ":app:desktop",
    ":app:ios-combine",
    ":app:macos",
    ":app:web",
    ":app:intellij-plugin",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

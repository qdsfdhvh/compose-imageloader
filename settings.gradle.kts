pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    includeBuild("build-logic")
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.develocity") version "3.19.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.9.0"
}

develocity {
    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing {
            onlyIf { System.getenv("GITHUB_ACTIONS") != null }
        }
    }
}

include(
    ":image-loader",
    ":image-loader-singleton",
    ":extension:compose-resources",
    ":extension:imageio",
    ":extension:ktor-network",
    ":extension:moko-resources",
    ":extension:nine-patch",
    ":extension:svg",
    ":app:common",
    ":app:android",
    ":app:android:benchmark",
    ":app:desktop",
    ":app:macos",
    ":app:web",
    ":app:wasmJs",
    ":app:intellij-plugin",
    ":test-paparazzi",
    ":test-roborazzi",
)

rootProject.name = "compose-imageLoader"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

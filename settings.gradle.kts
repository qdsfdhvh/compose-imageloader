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
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.17.4"
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

rootProject.name = "compose-imageLoader"

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
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

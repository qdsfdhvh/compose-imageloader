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
    id("com.gradle.enterprise") version "3.18.1"
    id("com.dropbox.focus") version "0.6.0"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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

configure<com.dropbox.focus.FocusExtension> {
    allSettingsFileName.set("includes.gradle.kts")
    focusFileName.set(".focus")
}

rootProject.name = "compose-imageLoader"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

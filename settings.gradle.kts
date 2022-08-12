pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    // error with js
    // repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "compose-imageLoader"
include(":image-loader")
include(
    ":app:common",
    ":app:android",
    ":app:desktop",
    ":app:ios",
    ":app:macos",
    ":app:web",
)

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

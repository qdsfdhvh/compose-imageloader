plugins {
    `kotlin-dsl`
    alias(libs.plugins.spotless)
}

kotlin {
    jvmToolchain(17)
}

spotless {
    kotlin {
        target("src/**/*.kt")
        targetExclude("**/build/")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("*.kts")
        targetExclude("**/build/")
        ktlint(libs.versions.ktlint.get())
    }
}

dependencies {
    compileOnly(libs.bundles.logic.plugins)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "app.kotlin.multiplatform"
            implementationClass = "KotlinMultiplatformConventionPlugin"
        }
        register("kotlinAndroid") {
            id = "app.kotlin.android"
            implementationClass = "KotlinAndroidConventionPlugin"
        }
        register("kotlinJvm") {
            id = "app.kotlin.jvm"
            implementationClass = "KotlinJvmConventionPlugin"
        }
        register("androidApplication") {
            id = "app.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "app.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidTest") {
            id = "app.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "app.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
    }
}

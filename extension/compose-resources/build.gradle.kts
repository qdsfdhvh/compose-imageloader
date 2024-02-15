plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                // implementation compose resources will auto enable generate res task, so here use compileOnly
                // https://github.com/JetBrains/compose-multiplatform/blob/master/gradle-plugins/compose/src/main/kotlin/org/jetbrains/compose/resources/ResourcesGenerator.kt#L143
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                compileOnly(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.compose.resources"
}

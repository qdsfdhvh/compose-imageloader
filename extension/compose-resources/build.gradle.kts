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
                // https://github.com/JetBrains/compose-multiplatform/blob/dea37a012d06dd64bde9e1fb27e387145eb7d453/gradle-plugins/compose/src/main/kotlin/org/jetbrains/compose/resources/ComposeResources.kt#L34
                compileOnly(compose.components.resources)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.compose.resources"
}

plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.moko.resources)
}

kotlin {
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)

                api(projects.imageLoader)
                implementation(projects.extension.blur)
                implementation(projects.extension.mokoResources)

                implementation(libs.moko.resources)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kermit)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val desktopMain by getting {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        val appleMain by getting {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(libs.ktor.client.js)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.demo.common"
}

multiplatformResources {
    multiplatformResourcesPackage = "com.seiko.imageloader.demo"
}

// workaround
listOf(
    "iosSimulatorArm64ProcessResources",
    "iosX64ProcessResources",
    "macosArm64ProcessResources",
    "macosX64ProcessResources",
).forEach { name ->
    tasks.matching { it.name == name }.configureEach {
        dependsOn(tasks.matching { it.name == "generateMRcommonMain" })
    }
}

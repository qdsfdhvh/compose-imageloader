plugins {
    id("project-kmp")
    kotlin("plugin.serialization")
    // task error: Cannot change attributes of dependency configuration ':app:common:iosArm64ApiElements' after it has been resolved
    // id("dev.icerock.mobile.multiplatform-resources").version(Versions.multiplatformResources)
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
                api(compose.materialIconsExtended)

                api(projects.imageLoader)
                api(projects.extension.blur)

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

// multiplatformResources {
//     multiplatformResourcesPackage = "com.seiko.imageloader.demo"
// }

// skip task because it's failed on gradle 7 and we not use results of this processing
// tasks.getByName("iosArm64ProcessResources").enabled = false

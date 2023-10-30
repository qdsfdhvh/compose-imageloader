plugins {
    id("app.android.library")
    id("app.kotlin.multiplatform")
    id("app.compose.multiplatform")
    alias(libs.plugins.kotlin.plugin.serialization)
    alias(libs.plugins.moko.resources)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.ui)
                api(compose.material)

                api(projects.imageLoader)
                implementation(projects.extension.blur)
                implementation(projects.extension.mokoResources)
                implementation(projects.extension.ninePatch)

                implementation(libs.moko.resources)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.logging)
                implementation(libs.kermit)
            }
        }
        androidMain {
            // https://github.com/icerockdev/moko-resources/issues/531
            dependsOn(commonMain.get())
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        desktopMain {
            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }
        appleMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
        jsMain {
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

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    id("app.compose.multiplatform")
}

kotlin {
    macosX64 {
        binaries {
            executable {
                entryPoint = "com.seiko.imageloader.demo.main"
                freeCompilerArgs += listOf(
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "Metal",
                )
            }
        }
    }
    macosArm64 {
        binaries {
            executable {
                entryPoint = "com.seiko.imageloader.demo.main"
                freeCompilerArgs += listOf(
                    "-linker-option",
                    "-framework",
                    "-linker-option",
                    "Metal",
                )
            }
        }
    }
    @Suppress("UNUSED_VARIABLE")
    sourceSets {
        val macosMain by creating {
            dependencies {
                implementation(projects.app.common)
            }
        }
        val macosX64Main by getting {
            dependsOn(macosMain)
        }
        val macosArm64Main by getting {
            dependsOn(macosMain)
        }
    }
}

compose.desktop.nativeApplication {
    targets(kotlin.targets.getByName("macosX64"), kotlin.targets.getByName("macosArm64"))
    distributions {
        targetFormats(org.jetbrains.compose.desktop.application.dsl.TargetFormat.Dmg)
        packageName = "ComposeImageLoader"
        packageVersion = "1.0.0"
    }
}

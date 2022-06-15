

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version Versions.compose_jb
    // id("com.rickclephas.kmp.nativecoroutines") version "0.12.2-new-mm"
}

kotlin {
    iosX64("uikitX64") {
        binaries {
            executable {
                entryPoint = "com.seiko.imageloader.demo.main"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
            }
        }
    }
    iosArm64("uikitArm64") {
        binaries {
            executable {
                entryPoint = "com.seiko.imageloader.demo.main"
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-linker-option", "-framework", "-linker-option", "Metal",
                    "-linker-option", "-framework", "-linker-option", "CoreText",
                    "-linker-option", "-framework", "-linker-option", "CoreGraphics"
                )
                // TODO: the current compose binary surprises LLVM, so disable checks for now.
                freeCompilerArgs = freeCompilerArgs + "-Xdisable-phases=VerifyBitcode"
            }
        }
    }
    sourceSets {
        val uikitMain by creating {
            dependencies {
                implementation(projects.app.common)
            }
        }
        val uikitX64Main by getting {
            dependsOn(uikitMain)
        }
        val uikitArm64Main by getting {
            dependsOn(uikitMain)
        }
    }
}

compose.experimental {
    uikit.application {
        bundleIdPrefix = "com.seiko.imageloader.demo"
        projectName = "Compose ImageLoader"
    }
}

kotlin {
    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget> {
        binaries.all {
            // TODO: the current compose binary surprises LLVM, so disable checks for now.
            freeCompilerArgs = freeCompilerArgs + "-Xdisable-phases=VerifyBitcode"
        }
    }
}

// TODO: remove when https://youtrack.jetbrains.com/issue/KT-50778 fixed
project.tasks.withType(org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile::class.java).configureEach {
    kotlinOptions.freeCompilerArgs += listOf(
        "-Xir-dce-runtime-diagnostic=log"
    )
}

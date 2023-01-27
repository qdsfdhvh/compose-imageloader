import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    android {
        publishLibraryVariants("debug", "release")
    }
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = Versions.Java.jvmTarget
        }
    }
    ios()
    iosSimulatorArm64()
    macosX64()
    macosArm64()
    js(IR) {
        browser()
        nodejs()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.imageLoader)
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("com.github.android:renderscript-intrinsics-replacement-toolkit:b6363490c3")
            }
        }
        val skiaMain by creating {
            dependsOn(commonMain)
        }
        val desktopMain by getting {
            dependsOn(skiaMain)
        }
        val iosMain by getting {
            dependsOn(skiaMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(skiaMain)
        }
        val macosX64Main by getting {
            dependsOn(skiaMain)
        }
        val macosArm64Main by getting {
            dependsOn(skiaMain)
        }
        val jsMain by getting {
            dependsOn(skiaMain)
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.blur"
    compileSdk = Versions.Android.compile
    defaultConfig {
        minSdk = Versions.Android.min
    }
    compileOptions {
        sourceCompatibility = Versions.Java.source
        targetCompatibility = Versions.Java.target
    }
}

@Suppress("UnstableApiUsage")
mavenPublishing {
    configure(KotlinMultiplatform())
    pomFromGradleProperties()
}

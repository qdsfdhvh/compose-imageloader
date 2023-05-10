import com.vanniktech.maven.publish.KotlinMultiplatform

plugins {
    id("project-kmp")
    id("com.vanniktech.maven.publish.base")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.imageLoader)
                implementation(libs.moko.resources)
            }
        }
    }
}

android {
    namespace = "io.github.qdsfdhvh.imageloader.moko.resources"
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

import com.vanniktech.maven.publish.MavenPublishBaseExtension
import com.vanniktech.maven.publish.SonatypeHost

plugins {
    id("com.android.application") apply false
    id("com.android.library") apply false
    kotlin("android") apply false
    kotlin("plugin.serialization") version Versions.Kotlin.lang apply false
    id("org.jetbrains.compose") version Versions.compose_jb apply false
    id("com.diffplug.spotless") version Versions.spotless
    id("com.vanniktech.maven.publish") version "0.22.0" apply false
}

allprojects {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            jvmTarget = Versions.Java.jvmTarget
            freeCompilerArgs = freeCompilerArgs + listOf()
        }
    }

    apply(plugin = "com.diffplug.spotless")
    spotless {
        kotlin {
            target("**/*.kt")
            targetExclude("$buildDir/**/*.kt", "bin/**/*.kt", "buildSrc/**/*.kt")
            ktlint(Versions.ktlint)
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(Versions.ktlint)
        }
        java {
            target("**/*.java")
            targetExclude("$buildDir/**/*.java", "bin/**/*.java")
        }
    }

    group = "io.github.qdsfdhvh"
    version = "1.2.2.1"

    plugins.withId("com.vanniktech.maven.publish.base") {
        @Suppress("UnstableApiUsage")
        configure<MavenPublishBaseExtension> {
            publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
            signAllPublications()
            pom {
                name.set("compose-imageLoader")
                description.set("Compose ImageLoader.")
                url.set("https://github.com/qdsfdhvh/compose-imageLoader")
                licenses {
                    license {
                        name.set("MIT")
                        url.set("https://opensource.org/licenses/MIT")
                        distribution.set("repo")
                    }
                }
                developers {
                    developer {
                        id.set("Seiko")
                        name.set("SeikoDes")
                        email.set("seiko_des@outlook.com")
                    }
                }
                scm {
                    url.set("https://github.com/qdsfdhvh/compose-imageLoader")
                    connection.set("scm:git:git://github.com/qdsfdhvh/compose-imageLoader.git")
                    developerConnection.set("scm:git:git://github.com/qdsfdhvh/compose-imageLoader.git")
                }
            }
        }
    }
}

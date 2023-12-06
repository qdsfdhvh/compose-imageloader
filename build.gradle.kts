import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
}

spotless {
    kotlin {
        target("**/*.kt")
        targetExclude("**/build/")
        ktlint(libs.versions.ktlint.get())
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/")
        ktlint(libs.versions.ktlint.get())
    }
}

allprojects {
    group = "io.github.qdsfdhvh"
    version = ProjectVersion.version

    plugins.withId("com.vanniktech.maven.publish.base") {
        mavenPublishing {
            publishToMavenCentral(SonatypeHost.S01, automaticRelease = true)
            signAllPublications()
            @Suppress("UnstableApiUsage")
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
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
        kotlinOptions {
            if (findProperty("myapp.enableComposeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" +
                        project.layout.buildDirectory.dir("compose_metrics").get().asFile.absolutePath,
                )
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" +
                        project.layout.buildDirectory.dir("compose_metrics").get().asFile.absolutePath,
                )
            }
        }
    }
}

tasks.dokkaHtmlMultiModule {
    moduleVersion.set(ProjectVersion.version)
    outputDirectory.set(rootDir.resolve("docs/static/api"))
}

object ProjectVersion {
    // incompatible API changes
    private const val major = "1"

    // functionality in a backwards compatible manner
    private const val monir = "7"

    // backwards compatible bug fixes
    private const val path = "1"
    const val version = "$major.$monir.$path"
}

gradle.taskGraph.whenReady {
    if (project.hasProperty("noAppApple")) {
        allTasks.asSequence()
            .filter {
                it.path.startsWith(":app:ios-combine") ||
                    it.path.startsWith(":app:macos") ||
                    it.path.startsWith(":app:web")
            }
            .forEach {
                it.enabled = false
            }
    }
}

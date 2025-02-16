import com.vanniktech.maven.publish.SonatypeHost

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.plugin.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.roborazzi) apply false
    alias(libs.plugins.poko) apply false
    alias(libs.plugins.spotless)
    alias(libs.plugins.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.binaryCompatibilityValidator)
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

apiValidation {
    ignoredProjects.addAll(
        listOf(
            "android",
            "common",
            "desktop",
            "intellij-plugin",
            "test-paparazzi",
            "test-roborazzi",
        ),
    )
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

    // fix order of android release lint tasks
    listOf(
        "generateReleaseLintVitalModel",
        "lintVitalAnalyzeRelease",
    ).forEach { name ->
        tasks.matching { it.name == name }.configureEach {
            dependsOn(tasks.matching { it.name == "copyFontsToAndroidAssets" })
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
    private const val monir = "10"

    // backwards compatible bug fixes
    private const val path = "0"
    const val version = "$major.$monir.$path"
}

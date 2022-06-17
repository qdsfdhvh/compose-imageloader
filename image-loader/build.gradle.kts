import org.jetbrains.compose.compose
import java.util.Properties

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose").version(Versions.compose_jb)
    id("com.android.library")
    id("maven-publish")
    id("signing")
}

group = "io.github.qdsfdhvh"
version = "1.0.1"

kotlin {
    android()
    jvm()
    ios()
    iosArm64()
    iosX64()
    sourceSets {
        val commonMain by getting {
            kotlin.srcDir("src/commonMain/compose")
            dependencies {
                api(compose.foundation)
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.Kotlin.coroutines}")
                api("com.squareup.okio:okio:3.1.0")
                api("io.ktor:ktor-client-core:${Versions.ktor}")
                implementation("io.github.aakira:napier:2.6.1")
            }
        }
        val androidMain by getting {
            kotlin.srcDir("src/androidMain/gif")
            dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.Kotlin.coroutines}")
                implementation("androidx.core:core-ktx:1.8.0")
                implementation("androidx.compose.ui:ui-graphics:${Versions.compose}")
                implementation("androidx.exifinterface:exifinterface:1.3.3")
                implementation("androidx.appcompat:appcompat-resources:1.4.2")
                implementation("com.google.accompanist:accompanist-drawablepainter:0.23.1")
                // svg
                implementation("com.caverock:androidsvg-aar:1.4")
            }
        }
        val jvmMain by sourceSets.getting {
            dependencies {
                implementation("io.ktor:ktor-client-cio:${Versions.ktor}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${Versions.Kotlin.coroutines}")
                // svg
                implementation("com.twelvemonkeys.imageio:imageio-batik:3.8.2")
                implementation("org.apache.xmlgraphics:batik-transcoder:1.14")
            }
        }
        val iosMain by sourceSets.getting {
            dependencies {
                // why not CIO https://github.com/joreilly/PeopleInSpace/issues/69
                implementation("io.ktor:ktor-client-darwin:${Versions.ktor}")
            }
        }
        val iosArm64Main by sourceSets.getting
        val iosX64Main by sourceSets.getting
    }
}

android {
    compileSdk = Versions.Android.compile
    buildToolsVersion = Versions.Android.buildTools
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.Android.min
        targetSdk = Versions.Android.target
    }
    compileOptions {
        sourceCompatibility = Versions.Java.java
        targetCompatibility = Versions.Java.java
    }
}

ext {
    val publishPropFile = rootProject.file("publish.properties")
    if (publishPropFile.exists()) {
        Properties().apply {
            load(publishPropFile.inputStream())
        }.forEach { name, value ->
            set(name.toString(), value)
        }
    } else {
        set("signing.keyId", System.getenv("SIGNING_KEY_ID"))
        set("signing.password", System.getenv("SIGNING_PASSWORD"))
        set("signing.secretKeyRingFile", System.getenv("SIGNING_SECRET_KEY_RING_FILE"))
        set("ossrhUsername", System.getenv("OSSRH_USERNAME"))
        set("ossrhPassword", System.getenv("OSSRH_PASSWORD"))
    }
}

val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

publishing {
    signing {
        sign(publishing.publications)
    }
    repositories {
        maven {
            val releasesRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
            url = if (version.toString().endsWith("SNAPSHOT")) {
                uri(snapshotsRepoUrl)
            } else {
                uri(releasesRepoUrl)
            }
            credentials {
                username = project.ext.get("ossrhUsername").toString()
                password = project.ext.get("ossrhPassword").toString()
            }
        }
    }
    publications.withType<MavenPublication> {
        artifact(javadocJar.get())
        pom {
            name.set("compose-imageLoader")
            description.set("Compose ImageLoader.")
            url.set("https://github.com/qdsfdhvh/compose-imageLoader")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Seiko")
                    name.set("Seiko Des")
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

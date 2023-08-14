import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

fun Project.configureAndroid() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(Versions.compileSdk)
        defaultConfig {
            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
        }
        // Can remove this once https://issuetracker.google.com/issues/260059413 is fixed.
        // See https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_11
        }
        sourceSets.getByName("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
        }
    }
}

fun Project.configKotlin() {
    extensions.configure<KotlinTopLevelExtension> {
        jvmToolchain(11)
    }
}

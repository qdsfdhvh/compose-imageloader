import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
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
    }
}

fun Project.configKotlin() {
    extensions.configure<KotlinTopLevelExtension> {
        jvmToolchain(11)
    }
}

fun Project.configJava() {
    extensions.configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_11
    }
}

// TODO: Remove when ktor 3.0.0 rc
// https://youtrack.jetbrains.com/issue/KTOR-5587
fun Project.applyKtorWasmWorkaround(version: String) {
    configurations.all {
        if (name.startsWith("wasmJs")) {
            resolutionStrategy.eachDependency {
                if (requested.group.startsWith("io.ktor") &&
                    requested.name.startsWith("ktor-")
                ) {
                    useVersion(version)
                }
            }
        }
    }
}

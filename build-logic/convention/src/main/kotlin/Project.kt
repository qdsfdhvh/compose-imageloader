import com.android.build.gradle.BaseExtension
import org.gradle.api.Project
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

fun Project.configureAndroid() {
    extensions.configure<BaseExtension> {
        compileSdkVersion(Versions.compileSdk)
        defaultConfig {
            minSdk = Versions.minSdk
            targetSdk = Versions.targetSdk
        }
    }
}

fun Project.configKotlin() {
    extensions.configure<KotlinTopLevelExtension> {
        // https://kotlinlang.org/docs/gradle-configure-project.html#gradle-java-toolchains-support
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }
}

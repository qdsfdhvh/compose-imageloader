import org.gradle.api.JavaVersion

object Versions {

    object Android {
        const val min = 21
        const val compile = 33
        const val target = compile
    }

    object Kotlin {
        const val lang = "1.8.0"
        const val coroutines = "1.6.3"
        const val serialization = "1.3.3"
    }

    object Java {
        const val jvmTarget = "11"
        val target = JavaVersion.VERSION_11
        val source = JavaVersion.VERSION_17
    }

    const val spotless = "6.14.1"
    const val ktlint = "0.48.2"
    const val compose_jb = "1.3.0"
    const val multiplatformResources = "0.20.1"
    const val ktor = "2.2.3"
    const val okio = "3.3.0"
}
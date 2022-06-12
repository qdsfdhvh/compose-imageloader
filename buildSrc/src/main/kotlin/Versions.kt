import org.gradle.api.JavaVersion

object Versions {

    object Android {
        const val min = 21
        const val compile = 32
        const val target = compile
        const val buildTools = "32.0.0"
    }

    object Java {
        const val jvmTarget = "17"
        val java = JavaVersion.VERSION_17
    }

    const val compose_jb = "1.2.0-alpha01-dev709"
}
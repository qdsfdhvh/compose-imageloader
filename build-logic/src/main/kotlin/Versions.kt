import org.gradle.api.JavaVersion

object Versions {

    object Project {
        // incompatible API changes
        private const val major = "1"

        // functionality in a backwards compatible manner
        private const val monir = "5"

        // backwards compatible bug fixes
        private const val path = "2"
        const val version = "$major.$monir.$path"
    }

    object Android {
        const val min = 21
        const val compile = 34
        const val target = compile
    }

    object Java {
        const val jvmTarget = "17"
        val target = JavaVersion.VERSION_17
        val source = JavaVersion.VERSION_17
    }
}

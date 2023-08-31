import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.compose.ComposeExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.compose")
        }
        extensions.configure<ComposeExtension> {
            // https://github.com/JetBrains/compose-multiplatform/issues/3570
            // kotlinCompilerPlugin.set("1.5.2-beta01")
        }
    }
}

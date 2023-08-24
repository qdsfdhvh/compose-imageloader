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
            kotlinCompilerPlugin.set(dependencies.compiler.forKotlin("1.9.0"))
            kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.10")
        }
    }
}

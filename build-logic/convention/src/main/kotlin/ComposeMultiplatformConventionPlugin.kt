import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.compose.compiler.gradle.ComposeCompilerGradlePluginExtension

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }
        composeCompiler {
            // Enable 'strong skipping'
            // https://medium.com/androiddevelopers/jetpack-compose-strong-skipping-mode-explained-cbdb2aa4b900
            enableStrongSkippingMode.set(true)

            if (project.providers.gradleProperty("myapp.enableComposeCompilerReports").isPresent) {
                val composeReports = layout.buildDirectory.map { it.dir("reports").dir("compose") }
                reportsDestination.set(composeReports)
                metricsDestination.set(composeReports)
            }
        }
    }

    private fun Project.composeCompiler(block: ComposeCompilerGradlePluginExtension.() -> Unit) {
        extensions.configure<ComposeCompilerGradlePluginExtension>(block)
    }
}

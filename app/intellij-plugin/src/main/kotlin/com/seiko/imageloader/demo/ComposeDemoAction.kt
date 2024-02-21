package com.seiko.imageloader.demo

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.awt.ComposePanel
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.LocalImageLoader
import com.seiko.imageloader.cache.memory.maxSizePercent
import com.seiko.imageloader.component.setupDefaultComponents
import com.seiko.imageloader.demo.util.commonConfig
import com.seiko.imageloader.intercept.bitmapMemoryCacheConfig
import javax.swing.JComponent

class ComposeDemoAction : DumbAwareAction() {
    override fun actionPerformed(e: AnActionEvent) {
        e.project?.let {
            DemoDialog(it).show()
        }
    }

    class DemoDialog(private val project: Project) : DialogWrapper(project) {

        init {
            title = "Demo"
            init()
        }

        override fun createCenterPanel(): JComponent {
            return ComposePanel().apply {
                setBounds(0, 0, 800, 600)
                setContent {
                    CompositionLocalProvider(
                        LocalImageLoader provides remember { generateImageLoader() },
                    ) {
                        App()
                    }
                }
            }
        }

        @Suppress("UnstableApiUsage")
        private fun generateImageLoader(): ImageLoader {
            return ImageLoader {
                commonConfig()
                components {
                    setupDefaultComponents()
                }
                interceptor {
                    bitmapMemoryCacheConfig {
                        // Set the max size to 25% of the app's available memory.
                        maxSizePercent(0.25)
                    }
                }
            }
        }
    }
}

package com.seiko.imageloader.demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.ImageLoaderBuilder
import com.seiko.imageloader.LocalImageLoader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalImageLoader provides generateImageLoader()
            ) {
                App()
            }
        }
    }

    private fun generateImageLoader(): ImageLoader {
        return ImageLoaderBuilder(this)
            .build()
    }
}
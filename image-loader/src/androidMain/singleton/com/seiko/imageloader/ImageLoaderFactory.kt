package com.seiko.imageloader

import android.content.Context
import com.seiko.imageloader.component.setupDefaultComponents

interface ImageLoaderFactory {
    fun newImageLoader(): ImageLoader
}

private var currentImageLoader: ImageLoader? = null

fun clearContextImageLoader() {
    currentImageLoader = null
}

val Context.imageLoader: ImageLoader
    get() = currentImageLoader
        ?: newImageLoader().also {
            currentImageLoader = it
        }

@Synchronized
private fun Context.newImageLoader(): ImageLoader {
    currentImageLoader?.let { return it }
    return (applicationContext as? ImageLoaderFactory)?.newImageLoader()
        ?: ImageLoader {
            components {
                setupDefaultComponents(applicationContext)
            }
        }
}

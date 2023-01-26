package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.intercept.Interceptor

class ImageLoaderEngine internal constructor(
    val componentRegistry: ComponentRegistry,
    val interceptors: List<Interceptor>,
)

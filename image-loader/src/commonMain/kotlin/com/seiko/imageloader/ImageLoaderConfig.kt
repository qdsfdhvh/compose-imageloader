package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.InterceptorsBuilder
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.Logger
import com.seiko.imageloader.util.defaultImageScope
import kotlinx.coroutines.CoroutineScope

class ImageLoaderConfig internal constructor(
    val imageScope: CoroutineScope,
    val defaultOptions: Options,
    val engine: ImageLoaderEngine,
    val logger: Logger,
)

class ImageLoaderConfigBuilder internal constructor() {

    private var _imageScope: CoroutineScope? = null

    var imageScope: CoroutineScope
        get() = _imageScope ?: defaultImageScope.also { _imageScope = it }
        set(value) {
            _imageScope = value
        }

    var logger = Logger.None
    var options = Options()

    private val interceptorsBuilder = InterceptorsBuilder()
    private val componentsBuilder = ComponentRegistryBuilder()

    fun interceptor(builder: InterceptorsBuilder.() -> Unit) {
        interceptorsBuilder.run(builder)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) {
        componentsBuilder.run(builder)
    }

    internal fun build() = ImageLoaderConfig(
        imageScope = imageScope,
        logger = logger,
        defaultOptions = options,
        engine = ImageLoaderEngine(
            interceptors = interceptorsBuilder.build(),
            componentRegistry = componentsBuilder.build(),
        ),
    )
}

fun ImageLoaderConfig(block: ImageLoaderConfigBuilder.() -> Unit) =
    ImageLoaderConfigBuilder().apply(block).build()

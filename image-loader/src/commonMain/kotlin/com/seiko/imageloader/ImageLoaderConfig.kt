package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptor
import com.seiko.imageloader.intercept.InterceptorsBuilder
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.OptionsBuilder
import com.seiko.imageloader.util.Logger

class ImageLoaderConfig internal constructor(
    val defaultOptions: Options,
    val logger: Logger,
    val componentRegistry: ComponentRegistry,
    val interceptors: List<Interceptor>,
)

class ImageLoaderConfigBuilder internal constructor() {

    var logger = Logger.None

    private val interceptorsBuilder = InterceptorsBuilder()
    private val componentsBuilder = ComponentRegistryBuilder()
    private val optionsBuilder = OptionsBuilder()

    fun interceptor(builder: InterceptorsBuilder.() -> Unit) {
        interceptorsBuilder.run(builder)
    }

    fun components(builder: ComponentRegistryBuilder.() -> Unit) {
        componentsBuilder.run(builder)
    }

    fun options(builder: OptionsBuilder.() -> Unit) {
        optionsBuilder.run(builder)
    }

    internal fun build() = ImageLoaderConfig(
        logger = logger,
        defaultOptions = optionsBuilder.build(),
        interceptors = interceptorsBuilder.build(),
        componentRegistry = componentsBuilder.build(),
    )
}

fun ImageLoaderConfig(block: ImageLoaderConfigBuilder.() -> Unit) =
    ImageLoaderConfigBuilder().apply(block).build()

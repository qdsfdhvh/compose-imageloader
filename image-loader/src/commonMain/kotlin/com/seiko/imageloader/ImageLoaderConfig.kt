package com.seiko.imageloader

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.component.ComponentRegistryBuilder
import com.seiko.imageloader.intercept.Interceptors
import com.seiko.imageloader.intercept.InterceptorsBuilder
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.OptionsBuilder
import com.seiko.imageloader.util.Logger

class ImageLoaderConfig internal constructor(
    val logger: Logger,
    val defaultOptions: Options,
    val componentRegistry: ComponentRegistry,
    val interceptors: Interceptors,
)

class ImageLoaderConfigBuilder internal constructor() {

    var logger = Logger.None

    private val interceptorsBuilder = InterceptorsBuilder()
    private val componentsBuilder = ComponentRegistryBuilder()
    private val optionsBuilder = OptionsBuilder()

    @Suppress("NOTHING_TO_INLINE")
    inline fun takeFrom(
        imageLoader: ImageLoader,
        clearOptionsExtraData: Boolean = false,
        clearComponents: Boolean = false,
        clearInterceptors: Boolean = false,
        clearMemoryCaches: Boolean = false,
    ) {
        takeFrom(
            config = imageLoader.config,
            clearOptionsExtraData = clearOptionsExtraData,
            clearComponents = clearComponents,
            clearInterceptors = clearInterceptors,
            clearMemoryCaches = clearMemoryCaches,
        )
    }

    fun takeFrom(
        config: ImageLoaderConfig,
        clearOptionsExtraData: Boolean = false,
        clearComponents: Boolean = false,
        clearInterceptors: Boolean = false,
        clearMemoryCaches: Boolean = false,
    ) {
        logger = config.logger
        options {
            takeFrom(
                options = config.defaultOptions,
                clearOptionsExtraData = clearOptionsExtraData,
            )
        }
        components {
            takeFrom(
                componentRegistry = config.componentRegistry,
                clearComponents = clearComponents,
            )
        }
        interceptor {
            takeFrom(
                interceptors = config.interceptors,
                clearInterceptors = clearInterceptors,
                clearMemoryCaches = clearMemoryCaches,
            )
        }
    }

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

package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options

interface Interceptor {

    suspend fun intercept(chain: Chain): ImageResult

    interface Chain {
        val initialRequest: ImageRequest
        val config: ImageLoaderConfig

        val request: ImageRequest
        val options: Options
            get() =
                config.defaultOptions.also {
                    request.optionsBuilders.forEach { builder ->
                        it.run(builder)
                    }
                }

        val components: ComponentRegistry

        /**
         * Continue executing the chain.
         *
         * @param request The request to proceed with.
         */
        suspend fun proceed(request: ImageRequest): ImageResult
    }
}

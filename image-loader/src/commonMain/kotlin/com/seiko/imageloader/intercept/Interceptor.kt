package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.Logger

interface Interceptor {

    suspend fun intercept(chain: Chain): ImageResult

    interface Chain {
        val request: ImageRequest

        val logger: Logger
        val options: Options
        val components: ComponentRegistry

        suspend fun emit(action: ImageAction)

        /**
         * Continue executing the chain.
         *
         * @param request The request to proceed with.
         */
        suspend fun proceed(request: ImageRequest): ImageResult
    }
}

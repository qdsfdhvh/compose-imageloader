package com.seiko.imageloader.intercept

import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.request.Options


fun interface Interceptor {

    suspend fun intercept(chain: Chain): ImageResult

    companion object {
        inline operator fun invoke(crossinline block: (chain: Chain) -> ImageResult): Interceptor =
            Interceptor { block(it) }
    }

    interface Chain {

        val initialRequest: ImageRequest
        val request: ImageRequest
        val options: Options
        val components: ComponentRegistry

        // val size: Size

        // /**
        //  * Set the requested [Size] to load the image at.
        //  *
        //  * @param size The requested size for the image.
        //  */
        // fun withSize(size: Size): Chain

        /**
         * Continue executing the chain.
         *
         * @param request The request to proceed with.
         */
        suspend fun proceed(request: ImageRequest): ImageResult
    }
}

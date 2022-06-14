package com.seiko.imageloader.intercept

import com.seiko.imageloader.request.ImageRequest
import com.seiko.imageloader.request.ImageResult
import com.seiko.imageloader.size.Size


fun interface Interceptor {

    suspend fun intercept(chain: Chain): ImageResult

    interface Chain {

        val request: ImageRequest

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

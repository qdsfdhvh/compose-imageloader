package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.takeFrom
import kotlinx.coroutines.flow.FlowCollector

internal class InterceptorChainHelper(
    initialImageRequest: ImageRequest,
    private val initialOptions: Options,
    private val config: ImageLoaderConfig,
    private val flowCollector: FlowCollector<ImageAction>,
) {
    val logger get() = config.logger

    val interceptors by lazy {
        initialImageRequest.interceptors?.plus(config.interceptors.list)
            ?: config.interceptors.list
    }

    val components by lazy {
        initialImageRequest.components?.merge(config.componentRegistry)
            ?: config.componentRegistry
    }

    fun getOptions(request: ImageRequest): Options {
        return Options(initialOptions) {
            takeFrom(request)
        }
    }

    suspend fun emit(action: ImageAction) {
        flowCollector.emit(action)
    }
}

package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.option.Options
import kotlinx.coroutines.flow.FlowCollector

internal class InterceptorChainHelper(
    initialImageRequest: ImageRequest,
    private val config: ImageLoaderConfig,
    private val flowCollector: FlowCollector<ImageAction>,
) {
    val logger get() = config.logger

    private val interceptors by lazy {
        initialImageRequest.interceptors?.plus(config.interceptors.list)
            ?: config.interceptors.list
    }

    val components by lazy {
        initialImageRequest.components?.merge(config.componentRegistry)
            ?: config.componentRegistry
    }

    fun getOptions(request: ImageRequest): Options {
        return Options(config.defaultOptions) {
            request.optionsBuilders.forEach { builder ->
                builder.invoke(this)
            }
        }
    }

    fun getInterceptor(index: Int): Interceptor {
        return interceptors[index]
    }

    suspend fun emit(action: ImageAction) {
        flowCollector.emit(action)
    }
}

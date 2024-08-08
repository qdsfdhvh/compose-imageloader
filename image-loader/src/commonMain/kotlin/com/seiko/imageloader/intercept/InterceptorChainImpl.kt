package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.takeFrom
import com.seiko.imageloader.util.Logger
import kotlinx.coroutines.flow.FlowCollector

fun createInterceptorChain(
    initialRequest: ImageRequest,
    initialOptions: Options,
    config: ImageLoaderConfig,
    flowCollector: FlowCollector<ImageAction>?,
): Interceptor.Chain {
    return InterceptorChainImpl(
        helper = InterceptorChainHelper(
            initialImageRequest = initialRequest,
            initialOptions = initialOptions,
            config = config,
            flowCollector = flowCollector,
        ),
        request = initialRequest,
        index = 0,
    )
}

private class InterceptorChainImpl(
    private val helper: InterceptorChainHelper,
    override val request: ImageRequest,
    private val index: Int,
) : Interceptor.Chain {

    private fun copy(index: Int, request: ImageRequest) = InterceptorChainImpl(
        helper = helper,
        request = request,
        index = index,
    )

    override suspend fun proceed(request: ImageRequest): ImageResult {
        val interceptor = helper.interceptors[index]
        val chain = copy(index = index + 1, request = request)
        return interceptor.intercept(chain)
    }

    override val logger: Logger
        get() = helper.logger

    override val options: Options
        get() = helper.getOptions(request)

    override val components: ComponentRegistry
        get() = helper.components

    override suspend fun emit(action: ImageAction) {
        helper.emit(action)
    }
}

private class InterceptorChainHelper(
    initialImageRequest: ImageRequest,
    private val initialOptions: Options,
    private val config: ImageLoaderConfig,
    private val flowCollector: FlowCollector<ImageAction>?,
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
        flowCollector?.emit(action)
    }
}

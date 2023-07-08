package com.seiko.imageloader.intercept

import com.seiko.imageloader.ImageLoaderConfig
import com.seiko.imageloader.component.ComponentRegistry
import com.seiko.imageloader.model.ImageAction
import com.seiko.imageloader.model.ImageRequest
import com.seiko.imageloader.model.ImageResult
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.Logger
import kotlinx.coroutines.flow.FlowCollector

internal class InterceptorChainImpl(
    private val helper: InterceptorChainHelper,
    override val request: ImageRequest,
    private val index: Int,
) : Interceptor.Chain {

    constructor(
        initialRequest: ImageRequest,
        config: ImageLoaderConfig,
        flowCollector: FlowCollector<ImageAction>,
    ) : this(
        helper = InterceptorChainHelper(
            initialImageRequest = initialRequest,
            config = config,
            flowCollector = flowCollector,
        ),
        request = initialRequest,
        index = 0,
    )

    private fun copy(index: Int, request: ImageRequest) = InterceptorChainImpl(
        helper = helper,
        request = request,
        index = index,
    )

    override suspend fun proceed(request: ImageRequest): ImageResult {
        val interceptor = helper.getInterceptor(index)
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

package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.KtorRequestData
import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.ktorRequestData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.HttpMethod
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.utils.io.ByteReadChannel
import okio.BufferedSource

class KtorUrlFetcher private constructor(
    private val httpUrl: Url,
    httpClient: () -> HttpClient,
    private val ktorRequestData: KtorRequestData?,
) : Fetcher {

    private val httpClient by lazy(httpClient)

    override suspend fun fetch(): FetchResult {
        val response = httpClient.request {
            url(httpUrl)
            method = ktorRequestData?.method ?: HttpMethod.Get
            ktorRequestData?.headers?.let {
                headers {
                    it.forEach { (key, value) ->
                        append(key, value)
                    }
                }
            }
        }
        if (response.status.isSuccess()) {
            return FetchResult.OfSource(
                source = response.bodyAsChannel().source(),
                extra = extraData {
                    mimeType(response.contentType()?.toString())
                },
            )
        }
        throw KtorUrlRequestException("code:${response.status.value}, ${response.status.description}")
    }

    class Factory(
        private val httpClient: () -> HttpClient,
    ) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data !is Url) return null
            return KtorUrlFetcher(
                httpUrl = data,
                httpClient = httpClient,
                ktorRequestData = options.ktorRequestData,
            )
        }
    }

    companion object {
        val defaultHttpEngineFactory: () -> HttpClient
            get() = { HttpClient(httpEngine) }
    }
}

private class KtorUrlRequestException(msg: String) : RuntimeException(msg)

internal expect suspend fun ByteReadChannel.source(): BufferedSource

internal expect val httpEngine: HttpClientEngine

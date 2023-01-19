package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.model.extraData
import com.seiko.imageloader.model.mimeType
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.source
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.isSuccess

open class KtorUrlFetcher(
    private val httpUrl: Url,
    private val httpClient: Lazy<HttpClient>,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val response = httpClient.value.request {
            url(httpUrl)
        }
        if (response.status.isSuccess()) {
            return FetchResult.Source(
                source = response.bodyAsChannel().source(),
                extra = extraData {
                    mimeType(response.contentType()?.toString())
                }
            )
        }
        throw KtorUrlRequestException("code:${response.status.value}, ${response.status.description}")
    }

    class Factory(
        private val httpClient: Lazy<HttpClient>,
    ) : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data is Url) return KtorUrlFetcher(data, httpClient)
            return null
        }
    }
}

private class KtorUrlRequestException(msg: String) : RuntimeException(msg)

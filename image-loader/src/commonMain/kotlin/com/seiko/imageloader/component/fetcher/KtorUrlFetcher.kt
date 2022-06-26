package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.source
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Url
import io.ktor.http.contentType

class KtorUrlFetcher(
    private val httpUrl: Url,
    private val httpClient: Lazy<HttpClient>,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val response = httpClient.value.request {
            url(httpUrl)
        }
        return FetchSourceResult(
            source = response.bodyAsChannel().source(),
            mimeType = response.contentType()?.toString(),
        )
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

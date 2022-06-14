package com.seiko.imageloader.component.fetcher

import com.seiko.imageloader.request.Options
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.utils.io.jvm.javaio.toInputStream
import okio.buffer
import okio.source

class KtorUrlFetcher(
    private val httpUrl: Url,
    private val httpClient: Lazy<HttpClient>,
) : Fetcher {

    override suspend fun fetch(): FetchResult {
        val response = httpClient.value.request {
            url(httpUrl)
        }
        val mimeType = response.contentType()?.toString()
        Napier.d { "mineType= $mimeType" }

        return FetchSourceResult(
            source = response.bodyAsChannel().toInputStream().source().buffer(),
            mimeType = mimeType,
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

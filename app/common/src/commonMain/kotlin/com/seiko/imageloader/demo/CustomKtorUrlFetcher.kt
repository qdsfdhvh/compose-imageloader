package com.seiko.imageloader.demo

import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.fetcher.KtorUrlFetcher
import com.seiko.imageloader.option.Options
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.headers
import io.ktor.http.Url

class CustomKtorUrlFetcher(
    httpUrl: Url,
    httpClient: Lazy<HttpClient>,
) : KtorUrlFetcher(httpUrl, httpClient) {

    class Factory : Fetcher.Factory {
        override fun create(data: Any, options: Options): Fetcher? {
            if (data is Url) return KtorUrlFetcher(data, httpClient)
            return null
        }
    }
}

private val httpClient = lazy {
    Napier.base(DebugAntilog())
    HttpClient(httpEngine) {
        defaultRequest {
            headers {
                append("Custom", "AAA")
            }
        }
        install(Logging) {
            level = LogLevel.HEADERS
            logger = object : Logger {
                override fun log(message: String) {
                    Napier.d(tag = "HttpClient") { message }
                }
            }
        }
    }
}

expect val httpEngine: HttpClientEngine

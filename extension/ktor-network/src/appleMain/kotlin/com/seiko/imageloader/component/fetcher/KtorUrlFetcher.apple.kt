package com.seiko.imageloader.component.fetcher

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

internal actual val httpEngine: HttpClientEngine get() = Darwin.create()

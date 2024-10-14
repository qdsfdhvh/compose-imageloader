package com.seiko.imageloader.component.fetcher

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

internal actual val httpEngine: HttpClientEngine
    get() = Js.create()

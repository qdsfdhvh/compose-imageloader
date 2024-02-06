package com.seiko.imageloader.demo.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

actual val httpEngine: HttpClientEngine
    get() = OkHttp.create()

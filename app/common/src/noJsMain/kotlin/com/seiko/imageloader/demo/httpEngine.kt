package com.seiko.imageloader.demo

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual val httpEngine: HttpClientEngine
    get() = CIO.create()

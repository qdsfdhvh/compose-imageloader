package com.seiko.imageloader.demo.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.cio.CIO

actual val httpEngine: HttpClientEngine
    get() = CIO.create()

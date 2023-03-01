package com.seiko.imageloader.demo.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

actual val httpEngine: HttpClientEngine
    get() = Darwin.create()

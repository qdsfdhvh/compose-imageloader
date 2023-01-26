package com.seiko.imageloader.demo.util

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.js.Js

actual val httpEngine: HttpClientEngine
    get() = Js.create()

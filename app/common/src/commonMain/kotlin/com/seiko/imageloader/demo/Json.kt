package com.seiko.imageloader.demo

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString

val JSON = Json {
    ignoreUnknownKeys = true
    isLenient = true
    coerceInputValues = true
}

inline fun <reified T> String.decodeJson(): T = JSON.decodeFromString(this)

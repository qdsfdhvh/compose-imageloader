package com.seiko.imageloader.util

internal fun Any.parseString(maxLength: Int = 50): String {
    return if (this is String && length > maxLength) "${substring(0, maxLength)}..." else toString()
}

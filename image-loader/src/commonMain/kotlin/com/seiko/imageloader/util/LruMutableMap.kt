package com.seiko.imageloader.util

/**
 * Create a [MutableMap] that orders its entries by most recently used to least recently used.
 *
 * https://youtrack.jetbrains.com/issue/KT-52183
 */
@Suppress("FunctionName")
internal expect fun <K : Any, V : Any> LruMutableMap(
    initialCapacity: Int = 0,
    loadFactor: Float = 0.75F,
): MutableMap<K, V>

package com.seiko.imageloader.util

/**
 * Create a [MutableMap] that orders its entries by most recently used to least recently used.
 *
 * https://youtrack.jetbrains.com/issue/KT-52183
 */
@Suppress("FunctionName")
internal actual fun <K : Any, V : Any> LruMutableMap(
    initialCapacity: Int,
    loadFactor: Float,
): MutableMap<K, V> {
    return LinkedHashMap(initialCapacity, loadFactor, true)
}

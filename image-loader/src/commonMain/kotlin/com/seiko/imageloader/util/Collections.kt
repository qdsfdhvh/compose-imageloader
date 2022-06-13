package com.seiko.imageloader.util

/**
 * Functionally the same as [Iterable.forEach] except it generates
 * an index-based loop that doesn't use an [Iterator].
 */
internal inline fun <T> List<T>.forEachIndices(action: (T) -> Unit) {
    for (i in indices) {
        action(get(i))
    }
}

/**
 * Functionally the same as [Iterable.forEachIndexed] except it generates
 * an index-based loop that doesn't use an [Iterator].
 */
internal inline fun <T> List<T>.forEachIndexedIndices(action: (Int, T) -> Unit) {
    for (i in indices) {
        action(i, get(i))
    }
}

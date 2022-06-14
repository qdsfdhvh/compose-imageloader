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

/**
 * Return the first non-null value returned by [transform].
 * Generate an index-based loop that doesn't use an [Iterator].
 */
internal inline fun <R, T> List<R>.firstNotNullOfOrNullIndices(transform: (R) -> T?): T? {
    for (i in indices) {
        transform(get(i))?.let { return it }
    }
    return null
}

/**
 * Removes values from the list as determined by the [predicate].
 * Generate an index-based loop that doesn't use an [Iterator].
 */
internal inline fun <T> MutableList<T>.removeIfIndices(predicate: (T) -> Boolean) {
    var numDeleted = 0
    for (rawIndex in indices) {
        val index = rawIndex - numDeleted
        if (predicate(get(index))) {
            removeAt(index)
            numDeleted++
        }
    }
}

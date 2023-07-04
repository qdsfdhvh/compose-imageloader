package com.seiko.imageloader.util

internal actual class LruHashMap<K : Any, V : Any> actual constructor(
    initialCapacity: Int,
    loadFactor: Float,
) {

    actual constructor(original: LruHashMap<out K, V>) : this(
        /*
         * We can't call the primary constructor without passing values,
         * even though the expect constructor has all default values.
         * See https://youtrack.jetbrains.com/issue/KT-52193.
         */
        initialCapacity = 16,
        loadFactor = 0.75F,
    ) {
        for ((key, value) in original.entries) {
            put(key, value)
        }
    }

    private val map = LinkedHashMap<K, V>(initialCapacity, loadFactor)

    actual val isEmpty: Boolean get() = map.isEmpty()
    actual val entries: Set<Map.Entry<K, V>> get() = map.entries

    /**
     * Works similarly to Java LinkedHashMap with LRU order enabled.
     * Removes the existing item from the map if there is one, and then adds it back,
     * so the item is moved to the end.
     */
    actual operator fun get(key: K): V? {
        val item = map.remove(key)
        if (item != null) {
            map[key] = item
        }

        return item
    }

    /**
     * Works similarly to Java LinkedHashMap with LRU order enabled.
     * Removes the existing item from the map if there is one,
     * then inserts the new item to the map, so the item is moved to the end.
     */
    actual fun put(key: K, value: V): V? {
        val item = map.remove(key)
        map[key] = value

        return item
    }

    actual fun remove(key: K): V? = map.remove(key)
}

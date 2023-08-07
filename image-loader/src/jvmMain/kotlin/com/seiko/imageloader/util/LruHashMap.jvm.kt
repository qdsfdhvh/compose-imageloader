package com.seiko.imageloader.util

internal actual class LruHashMap<K : Any, V : Any> actual constructor(
    initialCapacity: Int,
    loadFactor: Float,
) {

    actual constructor(original: LruHashMap<out K, V>) : this() {
        for ((key, value) in original.entries) {
            put(key, value)
        }
    }

    private val map = LinkedHashMap<K, V>(initialCapacity, loadFactor, true)

    actual val isEmpty: Boolean get() = map.isEmpty()
    actual val entries: Set<Map.Entry<K, V>> get() = map.entries

    actual operator fun get(key: K): V? = map[key]

    actual fun put(key: K, value: V): V? = map.put(key, value)

    actual fun remove(key: K): V? = map.remove(key)
}

package com.seiko.imageloader.util

expect open class LruCache<K : Any, V : Any>(maxSize: Int) {
    protected open fun sizeOf(key: K, value: V): Int
    protected open fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?)
    protected open fun create(key: K): V?
    fun size(): Int
    fun maxSize(): Int
    fun snapshot(): MutableMap<K, V>
    operator fun get(key: K): V?
    fun put(key: K, value: V): V?
    fun remove(key: K): V?
    fun evictAll()
}

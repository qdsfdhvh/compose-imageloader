package com.seiko.imageloader.util

import kotlin.jvm.Synchronized

actual open class LruCache<K : Any, V : Any> actual constructor(maxSize: Int) {

    var maxSize = 0
        private set

        @Synchronized get
    var size = 0
        private set

        @Synchronized get
    var putCount = 0
        private set

        @Synchronized get
    var createCount = 0
        private set

        @Synchronized get
    var evictionCount = 0
        private set

        @Synchronized get
    var hitCount = 0
        private set

        @Synchronized get
    var missCount = 0
        private set

        @Synchronized get

    actual fun size() = size

    actual fun maxSize() = maxSize

    private val map: LinkedHashMap<K, V>

    private val syncObject = LockObject()

    init {
        require(maxSize > 0) { "maxSize <= 0" }
        this.maxSize = maxSize
        this.map = LinkedHashMap(0, 0.75f)
    }

    open fun resize(maxSize: Int) {
        require(maxSize > 0) { "maxSize <= 0" }
        synchronized(syncObject) { this.maxSize = maxSize }
        trimToSize(maxSize)
    }

    actual operator fun get(key: K): V? {
        if (key == null) {
            throw NullPointerException("key == null")
        }

        var mapValue: V? = null
        synchronized(syncObject) {
            mapValue = map[key]
            if (mapValue != null) {
                hitCount++
                return mapValue
            }
            missCount++
        }

        /*
         * Attempt to create a value. This may take a long time, and the map
         * may be different when create() returns. If a conflicting value was
         * added to the map while create() was working, we leave that value in
         * the map and release the created value.
         */
        val createdValue: V = create(key) ?: return null
        synchronized(syncObject) {
            createCount++
            mapValue = map.put(key, createdValue)
            if (mapValue != null) {
                // There was a conflict so undo that last put
                map.put(key, mapValue!!)
            } else {
                size += safeSizeOf(key, createdValue)
            }
        }
        return if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue)
            mapValue
        } else {
            trimToSize(maxSize)
            createdValue
        }
    }

    actual fun put(key: K, value: V): V? {
        if (key == null || value == null) {
            throw NullPointerException("key == null || value == null")
        }
        var previous: V? = null
        synchronized(syncObject) {
            putCount++
            size += safeSizeOf(key, value)
            previous = map.put(key, value)
            previous?.let {
                size -= safeSizeOf(key, it)
            }
        }
        previous?.let {
            entryRemoved(false, key, it, value)
        }
        trimToSize(maxSize)
        return previous
    }

    open fun trimToSize(maxSize: Int) {
        while (true) {
            var key: K? = null
            var value: V? = null
            synchronized(syncObject) {
                check(!(size < 0 || map.isEmpty() && size != 0)) {
                    (
                        this::class.simpleName +
                            ".sizeOf() is reporting inconsistent results!"
                        )
                }
                if (size <= maxSize || map.isEmpty()) {
                    return
                }
                val (key1, value1) = map.entries.iterator().next()
                key = key1
                value = value1
                map.remove(key)
                size -= safeSizeOf(key!!, value!!)
                evictionCount++
            }
            entryRemoved(true, key!!, value!!, null)
        }
    }

    actual fun remove(key: K): V? {
        if (key == null) {
            throw NullPointerException("key == null")
        }
        var previous: V? = null
        synchronized(syncObject) {
            previous = map.remove(key)
            previous?.let {
                size -= safeSizeOf(key, it)
            }
        }
        previous?.let {
            entryRemoved(false, key, it, null)
        }
        return previous
    }

    protected actual open fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?) = Unit

    protected actual open fun create(key: K): V? = null

    private fun safeSizeOf(key: K, value: V): Int {
        val result: Int = sizeOf(key, value)
        check(result >= 0) { "Negative size: $key=$value" }
        return result
    }

    protected actual open fun sizeOf(key: K, value: V): Int = 1

    actual fun evictAll() {
        trimToSize(-1) // -1 will evict 0-sized elements
    }

    @Synchronized
    actual fun snapshot(): MutableMap<K, V> = LinkedHashMap(map)
}

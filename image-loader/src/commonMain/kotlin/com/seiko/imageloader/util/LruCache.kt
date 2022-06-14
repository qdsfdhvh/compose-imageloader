package com.seiko.imageloader.util

internal open class LruCache<K, V>(maxSize: Int) {

    private var maxSize = 0
    private var size = 0
    private var putCount = 0
    private var createCount = 0
    private var evictionCount = 0
    private var hitCount = 0
    private var missCount = 0

    private val map: LinkedHashMap<K, V>

    init {
        require(maxSize > 0) { "maxSize <= 0" }
        this.maxSize = maxSize
        this.map = LinkedHashMap(0, 0.75f, true)
    }

    open fun resize(maxSize: Int) {
        require(maxSize > 0) { "maxSize <= 0" }
        synchronized(this) { this.maxSize = maxSize }
        trimToSize(maxSize)
    }

    operator fun get(key: K): V? {
        if (key == null) {
            throw NullPointerException("key == null")
        }

        var mapValue: V?
        synchronized(this) {
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
        synchronized(this) {
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

    fun put(key: K, value: V): V? {
        if (key == null || value == null) {
            throw java.lang.NullPointerException("key == null || value == null")
        }
        var previous: V?
        synchronized(this) {
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
            var key: K
            var value: V
            synchronized(this) {
                check(!(size < 0 || map.isEmpty() && size != 0)) {
                    (
                        javaClass.name +
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
                size -= safeSizeOf(key, value)
                evictionCount++
            }
            entryRemoved(true, key, value, null)
        }
    }

    fun remove(key: K): V? {
        if (key == null) {
            throw java.lang.NullPointerException("key == null")
        }
        var previous: V?
        synchronized(this) {
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

    protected open fun entryRemoved(evicted: Boolean, key: K, oldValue: V, newValue: V?) = Unit

    protected open fun create(key: K): V? = null

    private fun safeSizeOf(key: K, value: V): Int {
        val result: Int = sizeOf(key, value)
        check(result >= 0) { "Negative size: $key=$value" }
        return result
    }

    protected open fun sizeOf(key: K, value: V): Int = 1

    fun evictAll() {
        trimToSize(-1) // -1 will evict 0-sized elements
    }

    @Synchronized
    fun size(): Int = size

    @Synchronized
    fun maxSize(): Int = maxSize

    @Synchronized
    fun hitCount(): Int = hitCount

    @Synchronized
    fun missCount(): Int = missCount

    @Synchronized
    fun createCount(): Int = createCount

    @Synchronized
    fun putCount(): Int = putCount

    @Synchronized
    fun evictionCount(): Int = evictionCount

    @Synchronized
    fun snapshot(): Map<K, V> = LinkedHashMap(map)
}

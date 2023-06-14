package com.seiko.imageloader.util

actual open class LruCache<K : Any, V : Any> actual constructor(maxSize: Int) {

    private var _maxSize = 0
    private var _size = 0
    private var _putCount = 0
    private var _createCount = 0
    private var _evictionCount = 0
    private var _hitCount = 0
    private var _missCount = 0

    private val map: LinkedHashMap<K, V>

    private val syncObject = LockObject()

    actual fun size() = synchronized(syncObject) { _size }

    actual fun maxSize() = synchronized(syncObject) { _maxSize }

    init {
        require(maxSize > 0) { "maxSize <= 0" }
        this._maxSize = maxSize
        this.map = LinkedHashMap(0, 0.75f)
    }

    open fun resize(maxSize: Int) {
        require(maxSize > 0) { "maxSize <= 0" }
        synchronized(syncObject) { this._maxSize = maxSize }
        trimToSize(maxSize)
    }

    actual operator fun get(key: K): V? {
        var mapValue: V? = null
        synchronized(syncObject) {
            mapValue = map[key]
            if (mapValue != null) {
                _hitCount++
                return mapValue
            }
            _missCount++
        }

        /*
         * Attempt to create a value. This may take a long time, and the map
         * may be different when create() returns. If a conflicting value was
         * added to the map while create() was working, we leave that value in
         * the map and release the created value.
         */
        val createdValue: V = create(key) ?: return null
        synchronized(syncObject) {
            _createCount++
            mapValue = map.put(key, createdValue)
            if (mapValue != null) {
                // There was a conflict so undo that last put
                map.put(key, mapValue!!)
            } else {
                _size += safeSizeOf(key, createdValue)
            }
        }
        return if (mapValue != null) {
            entryRemoved(false, key, createdValue, mapValue)
            mapValue
        } else {
            trimToSize(_maxSize)
            createdValue
        }
    }

    actual fun put(key: K, value: V): V? {
        var previous: V? = null
        synchronized(syncObject) {
            _putCount++
            _size += safeSizeOf(key, value)
            previous = map.put(key, value)
            previous?.let {
                _size -= safeSizeOf(key, it)
            }
        }
        previous?.let {
            entryRemoved(false, key, it, value)
        }
        trimToSize(_maxSize)
        return previous
    }

    open fun trimToSize(maxSize: Int) {
        while (true) {
            var key: K? = null
            var value: V? = null
            synchronized(syncObject) {
                check(!(_size < 0 || map.isEmpty() && _size != 0)) {
                    (
                        this::class.simpleName +
                            ".sizeOf() is reporting inconsistent results!"
                        )
                }
                if (_size <= maxSize || map.isEmpty()) {
                    return
                }
                val (key1, value1) = map.entries.iterator().next()
                key = key1
                value = value1
                map.remove(key)
                _size -= safeSizeOf(key!!, value!!)
                _evictionCount++
            }
            entryRemoved(true, key!!, value!!, null)
        }
    }

    actual fun remove(key: K): V? {
        var previous: V? = null
        synchronized(syncObject) {
            previous = map.remove(key)
            previous?.let {
                _size -= safeSizeOf(key, it)
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

    actual fun snapshot(): MutableMap<K, V> {
        val copy = LinkedHashMap<K, V>()
        synchronized(syncObject) {
            map.entries.forEach { (key, value) ->
                copy[key] = value
            }
        }
        return copy
    }
}

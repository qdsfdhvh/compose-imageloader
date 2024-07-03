package com.seiko.imageloader.util

// https://youtrack.jetbrains.com/issue/KT-66230/KJS-Wasm-Support-Weak-References-to-Kotlin-objects
// Original JS reference
external class WeakRef(target: JsAny) {
    fun deref(): JsAny
}

actual class WeakReference<T : Any>(private var ref: WeakRef?) {

    actual constructor(referred: T) : this(WeakRef(referred.toJsReference()))

    /** The weakly referenced object. If the garbage collector collected the object, this returns null. */
    actual fun get(): T? = ref?.deref()?.unsafeCast<JsReference<T>>()?.get()

    actual fun clear() {
        ref = null
    }
}

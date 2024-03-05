package com.seiko.imageloader.util

import androidx.compose.runtime.InternalComposeApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okio.FileSystem

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

internal actual val ioDispatcher: CoroutineDispatcher get() = Dispatchers.Default

actual val defaultFileSystem: FileSystem? get() = null

@OptIn(InternalComposeApi::class)
actual fun identityHashCode(instance: Any): Int {
    return androidx.compose.runtime.identityHashCode(instance)
}

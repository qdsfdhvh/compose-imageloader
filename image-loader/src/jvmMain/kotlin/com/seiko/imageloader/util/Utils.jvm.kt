package com.seiko.imageloader.util

internal actual inline val Any.identityHashCode: Int
    get() = System.identityHashCode(this)

package com.seiko.imageloader.util

internal inline val Any.identityHashCode: Int
    get() = System.identityHashCode(this)
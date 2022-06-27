package com.seiko.imageloader

internal actual inline val Image.identityHashCode: Int
    get() = System.identityHashCode(this)

package com.seiko.imageloader

internal actual inline val Bitmap.identityHashCode: Int
    get() = hashCode()

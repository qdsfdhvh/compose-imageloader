package com.seiko.imageloader.util

interface AnimationPainter {

    fun isPlay(): Boolean

    fun update(nanoTime: Long)
}

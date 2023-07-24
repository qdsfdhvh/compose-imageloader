package com.seiko.imageloader.util

interface AnimationPainter {

    fun isPlay(): Boolean

    fun update(frameTimeMillis: Long)
}

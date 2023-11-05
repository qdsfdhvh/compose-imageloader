package com.seiko.imageloader.util

interface AnimationPainter {

    fun isPlay(): Boolean

    fun nextPlay(): Boolean

    fun update(frameTimeMillis: Long)
}

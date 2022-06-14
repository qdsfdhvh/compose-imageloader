package com.seiko.imageloader.demo

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val urls: Urls,
    val color: String,
    val width: Int,
    val height: Int,
) {
    @Serializable
    data class Urls(
        val raw: String,
        val full: String,
        val regular: String,
        val small: String,
        val thumb: String,
    )

    val url: String get() = urls.regular
}

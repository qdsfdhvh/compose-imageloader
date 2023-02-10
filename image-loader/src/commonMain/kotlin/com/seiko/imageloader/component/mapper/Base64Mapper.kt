package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.option.Options
import io.ktor.util.decodeBase64Bytes

class Base64Image(
    val contentType: String?,
    val content: ByteArray,
)

class Base64Mapper : Mapper<Base64Image> {
    override fun map(data: Any, options: Options): Base64Image? {
        if (data !is String) return null
        if (!isApplicable(data)) return null
        return data.split(",").let {
            val contentType = it.firstOrNull()?.removePrefix("data:")?.removeSuffix(";base64")
            val content = it.last()
            Base64Image(
                contentType = contentType,
                content = content.decodeBase64Bytes(),
            )
        }
    }

    private fun isApplicable(data: String): Boolean {
        return data.startsWith("data:")
    }
}

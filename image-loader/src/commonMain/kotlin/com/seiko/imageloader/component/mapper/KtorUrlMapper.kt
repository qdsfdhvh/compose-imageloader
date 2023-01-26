package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.option.Options
import io.ktor.http.Url

class KtorUrlMapper : Mapper<Url> {
    override fun map(data: Any, options: Options): Url? {
        if (data !is String) return null
        if (!isApplicable(data)) return null
        return Url(data)
    }

    private fun isApplicable(data: String): Boolean {
        return data.startsWith("http")
    }
}

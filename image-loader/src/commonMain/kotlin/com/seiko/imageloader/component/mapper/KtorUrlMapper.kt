package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options
import io.ktor.http.Url

class KtorUrlMapper : Mapper<String, Url> {
    override fun map(data: String, options: Options): Url? {
        if (!isApplicable(data)) return null
        return Url(data)
    }

    private fun isApplicable(data: String): Boolean {
        return data.startsWith("http")
    }
}

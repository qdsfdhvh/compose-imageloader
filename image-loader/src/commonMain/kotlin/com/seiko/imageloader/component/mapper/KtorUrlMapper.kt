package com.seiko.imageloader.component.mapper

import com.seiko.imageloader.request.Options
import com.seiko.imageloader.util.logv
import io.ktor.http.Url

class KtorUrlMapper : Mapper<Url> {
    override fun map(data: Any, options: Options): Url? {
        if (data !is String) return null
        if (!isApplicable(data)) return null
        return Url(data).also {
            logv(
                tag = "KtorUrlMapper",
                data = data,
            ) { "mapper to KtorUrl" }
        }
    }

    private fun isApplicable(data: String): Boolean {
        return data.startsWith("http")
    }
}

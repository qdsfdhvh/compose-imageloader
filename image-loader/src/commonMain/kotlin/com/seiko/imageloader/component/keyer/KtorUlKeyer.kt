package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.request.Options
import io.ktor.http.Url

class KtorUlKeyer : Keyer {
    override fun key(data: Any, options: Options): String? {
        if (data !is Url) return null
        return data.toString()
    }
}
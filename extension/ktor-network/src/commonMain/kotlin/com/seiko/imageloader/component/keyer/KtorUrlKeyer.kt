package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.option.Options
import io.ktor.http.Url

class KtorUrlKeyer : Keyer {
    override fun key(data: Any, options: Options): String? {
        if (data !is Url) return null
        return data.toString()
    }
}

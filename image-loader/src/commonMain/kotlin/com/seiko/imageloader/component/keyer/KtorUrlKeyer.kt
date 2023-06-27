package com.seiko.imageloader.component.keyer

import com.seiko.imageloader.option.Options
import io.ktor.http.Url

class KtorUrlKeyer : Keyer {
    override fun key(data: Any, options: Options, type: Keyer.Type): String? {
        if (data !is Url) return null
        val playAnimeSuffix = when (type) {
            Keyer.Type.Memory -> if (!options.playAnimate) "-noPlay" else ""
            Keyer.Type.Disk -> ""
        }
        return data.toString() + playAnimeSuffix
    }
}

package com.seiko.imageloader.component

import com.seiko.imageloader.ImageLoader
import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper
import com.seiko.imageloader.request.Options
import com.seiko.imageloader.request.SourceResult
import com.seiko.imageloader.util.forEachIndices

class ComponentRegistry internal constructor(
    private val mappers: List<Mapper<out Any>>,
    private val keyers: List<Keyer>,
    private val fetcherFactories: List<Fetcher.Factory>,
    private val decoderFactories: List<Decoder.Factory>,
) {
    fun map(data: Any, options: Options): Any {
        var mappedData = data
        mappers.forEachIndices { mapper ->
            mapper.map(mappedData, options)?.let { mappedData = it }
        }
        return mappedData
    }

    fun key(data: Any, options: Options): String? {
        keyers.forEachIndices { keyer ->
            keyer.key(data, options)?.let { return it }
        }
        return null
    }

    fun fetch(
        data: Any,
        options: Options,
        imageLoader: ImageLoader,
        startIndex: Int = 0,
    ): Pair<Fetcher, Int> {
        for (index in startIndex until fetcherFactories.size) {
            val factory = fetcherFactories[index]
            factory.create(data, options, imageLoader)?.let { return it to index }
        }
        error { "Unable to create a fetcher that supports: $data" }
    }

    fun decode(
        source: SourceResult,
        options: Options,
        startIndex: Int = 0,
    ): Pair<Decoder, Int> {
        for (index in startIndex until decoderFactories.size) {
            val factory = decoderFactories[index]
            factory.create(source, options)?.let { return it to index }
        }
        error { "Unable to create a decoder that supports: $source" }
    }
}
package com.seiko.imageloader.component

import com.seiko.imageloader.Poko
import com.seiko.imageloader.component.decoder.DecodeSource
import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.forEachIndices

@Poko class ComponentRegistry internal constructor(
    val mappers: List<Mapper<out Any>>,
    val keyers: List<Keyer>,
    val fetcherFactories: List<Fetcher.Factory>,
    val decoderFactories: List<Decoder.Factory>,
) {
    internal fun merge(component: ComponentRegistry) = ComponentRegistry(
        mappers = mappers + component.mappers,
        keyers = keyers + component.keyers,
        fetcherFactories = fetcherFactories + component.fetcherFactories,
        decoderFactories = decoderFactories + component.decoderFactories,
    )

    fun map(data: Any, options: Options): Any {
        var mappedData = data
        mappers.forEachIndices { mapper ->
            mapper.map(mappedData, options)?.let { mappedData = it }
        }
        return mappedData
    }

    fun key(data: Any, options: Options, type: Keyer.Type): String? {
        keyers.forEachIndices { keyer ->
            keyer.key(data, options, type)?.let { return it }
        }
        return null
    }

    fun fetch(
        data: Any,
        options: Options,
        startIndex: Int = 0,
    ): Pair<Fetcher, Int> {
        for (index in startIndex until fetcherFactories.size) {
            val factory = fetcherFactories[index]
            factory.create(data, options)?.let { return it to index }
        }
        throw RuntimeException("Unable to create a fetcher that supports: $data")
    }

    fun decode(
        source: DecodeSource,
        options: Options,
        startIndex: Int = 0,
    ): Pair<Decoder, Int> {
        for (index in startIndex until decoderFactories.size) {
            val factory = decoderFactories[index]
            factory.create(source, options)?.let { return it to index }
        }
        throw RuntimeException("Unable to create a decoder that supports: $source")
    }
}

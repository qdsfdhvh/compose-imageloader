package com.seiko.imageloader.component

import com.seiko.imageloader.component.decoder.DecodeSource
import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.util.forEachIndices

class ComponentRegistry internal constructor(
    private val mappers: List<Mapper<out Any>>,
    private val keyers: List<Keyer>,
    private val fetcherFactories: List<Fetcher.Factory>,
    private val decoderFactories: List<Decoder.Factory>,
) {
    internal fun merge(component: ComponentRegistry) = ComponentRegistry(
        mappers = mappers + component.mappers,
        keyers = keyers + component.keyers,
        fetcherFactories = fetcherFactories + component.fetcherFactories,
        decoderFactories = decoderFactories + component.decoderFactories,
    )

    internal fun newBuilder() = ComponentRegistryBuilder(
        mappers = mappers.toMutableList(),
        keyers = keyers.toMutableList(),
        fetcherFactories = fetcherFactories.toMutableList(),
        decoderFactories = decoderFactories.toMutableList(),
    )

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
        startIndex: Int = 0,
    ): Pair<Fetcher, Int> {
        for (index in startIndex until fetcherFactories.size) {
            val factory = fetcherFactories[index]
            factory.create(data, options)?.let { return it to index }
        }
        throw RuntimeException("Unable to create a fetcher that supports: $data")
    }

    suspend fun decode(
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

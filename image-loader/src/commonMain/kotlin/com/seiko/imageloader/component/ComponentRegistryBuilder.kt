package com.seiko.imageloader.component

import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper

class ComponentRegistryBuilder {

    private val mappers = mutableListOf<Mapper<out Any, out Any>>()
    private val keyers = mutableListOf<Keyer<out Any>>()
    private val fetcherFactories = mutableListOf<Fetcher.Factory>()
    private val decoderFactories = mutableListOf<Decoder.Factory>()

    fun add(mapper: Mapper<out Any, out Any>) = apply {
        mappers.add(mapper)
    }

    fun add(keyer: Keyer<out Any>) = apply {
        keyers.add(keyer)
    }

    fun add(fetcherFactory: Fetcher.Factory) = apply {
        fetcherFactories.add(fetcherFactory)
    }

    fun add(decoderFactory: Decoder.Factory) = apply {
        decoderFactories.add(decoderFactory)
    }

    fun build(): ComponentRegistry = ComponentRegistry(
        mappers = mappers,
        keyers = keyers,
        fetcherFactories = fetcherFactories,
        decoderFactories = decoderFactories,
    )
}
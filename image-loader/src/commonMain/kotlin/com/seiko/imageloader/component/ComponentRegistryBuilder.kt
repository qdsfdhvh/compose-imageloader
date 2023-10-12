package com.seiko.imageloader.component

import com.seiko.imageloader.component.decoder.Decoder
import com.seiko.imageloader.component.fetcher.Fetcher
import com.seiko.imageloader.component.keyer.Keyer
import com.seiko.imageloader.component.mapper.Mapper

class ComponentRegistryBuilder internal constructor(
    private val mappers: MutableList<Mapper<out Any>> = mutableListOf(),
    private val keyers: MutableList<Keyer> = mutableListOf(),
    private val fetcherFactories: MutableList<Fetcher.Factory> = mutableListOf(),
    private val decoderFactories: MutableList<Decoder.Factory> = mutableListOf(),
) {
    internal constructor(componentRegistry: ComponentRegistry) : this(
        mappers = componentRegistry.mappers.toMutableList(),
        keyers = componentRegistry.keyers.toMutableList(),
        fetcherFactories = componentRegistry.fetcherFactories.toMutableList(),
        decoderFactories = componentRegistry.decoderFactories.toMutableList(),
    )

    fun takeFrom(
        componentRegistry: ComponentRegistry,
        clearComponents: Boolean = false,
    ) {
        if (clearComponents) {
            mappers.clear()
            keyers.clear()
            fetcherFactories.clear()
            decoderFactories.clear()
        }
        mappers.addAll(componentRegistry.mappers)
        keyers.addAll(componentRegistry.keyers)
        fetcherFactories.addAll(componentRegistry.fetcherFactories)
        decoderFactories.addAll(componentRegistry.decoderFactories)
    }

    fun add(mapper: Mapper<out Any>) {
        mappers.add(mapper)
    }

    fun add(keyer: Keyer) {
        keyers.add(keyer)
    }

    fun add(fetcherFactory: Fetcher.Factory) {
        fetcherFactories.add(fetcherFactory)
    }

    fun add(decoderFactory: Decoder.Factory) {
        decoderFactories.add(decoderFactory)
    }

    internal fun build(): ComponentRegistry = ComponentRegistry(
        mappers = mappers,
        keyers = keyers,
        fetcherFactories = fetcherFactories,
        decoderFactories = decoderFactories,
    )
}

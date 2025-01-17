package com.seiko.imageloader.cache.memory

import dev.drewhamilton.poko.Poko

@Poko
class MemoryKey(
    val key: String,
    val extra: Map<String, String>,
)

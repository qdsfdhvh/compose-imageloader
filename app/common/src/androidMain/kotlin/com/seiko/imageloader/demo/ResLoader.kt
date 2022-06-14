package com.seiko.imageloader.demo

import android.content.Context
import dev.icerock.moko.resources.AssetResource

actual class ResLoader(private val contextProvider: () -> Context) {

    actual fun getString(file: AssetResource): String {
        return file.readText(contextProvider())
    }
}
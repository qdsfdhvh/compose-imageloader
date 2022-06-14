package com.seiko.imageloader.demo

import dev.icerock.moko.resources.AssetResource

actual class ResLoader() {
    actual fun getString(file: AssetResource): String {
        return file.readText()
    }
}

package com.seiko.imageloader.util

import okio.FileSystem
import okio.Path.Companion.toOkioPath
import okio.Source
import java.io.File
import kotlin.random.Random

internal fun Source.tempFile(): File {
    val tempFile = File.createTempFile("temp_${Random.nextLong()}", null)
    tempFile.deleteOnExit()

    FileSystem.SYSTEM.write(tempFile.toOkioPath()) {
        writeAll(this@tempFile)
    }

    return tempFile
}

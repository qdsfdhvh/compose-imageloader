package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.option.Options
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource
import okio.buffer
import okio.source
import org.jetbrains.skiko.toImage
import java.io.FileNotFoundException

internal actual suspend fun AssetResource.toFetchResult(options: Options): FetchResult? {
    val stream = resourcesClassLoader.getResourceAsStream(filePath)
        ?: throw FileNotFoundException("Couldn't open resource as stream at: $filePath")
    return FetchResult.Source(
        source = stream.source().buffer(),
    )
}

internal actual suspend fun ColorResource.toFetchResult(options: Options): FetchResult? {
    return FetchResult.Painter(
        painter = ColorPainter(
            Color(
                red = lightColor.red,
                blue = lightColor.blue,
                green = lightColor.green,
                alpha = lightColor.alpha,
            ),
        ),
    )
}

internal actual suspend fun FileResource.toFetchResult(options: Options): FetchResult? {
    val stream = resourcesClassLoader.getResourceAsStream(filePath)
        ?: throw FileNotFoundException("Couldn't open resource as stream at: $filePath")
    return FetchResult.Source(
        source = stream.source().buffer(),
    )
}

internal actual suspend fun ImageResource.toFetchResult(options: Options): FetchResult? {
    return FetchResult.Image(
        image = image.toImage(),
    )
}

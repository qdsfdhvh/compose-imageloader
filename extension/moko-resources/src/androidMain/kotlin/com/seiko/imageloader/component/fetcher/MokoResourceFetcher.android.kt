package com.seiko.imageloader.component.fetcher

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import com.seiko.imageloader.model.ImageSourceFrom
import com.seiko.imageloader.model.toImageSource
import com.seiko.imageloader.option.Options
import com.seiko.imageloader.option.androidContext
import com.seiko.imageloader.toImage
import dev.icerock.moko.resources.AssetResource
import dev.icerock.moko.resources.ColorResource
import dev.icerock.moko.resources.FileResource
import dev.icerock.moko.resources.ImageResource

internal actual suspend fun AssetResource.toFetchResult(options: Options): FetchResult? {
    return FetchResult.OfSource(
        imageSource = getInputStream(options.androidContext).toImageSource(),
        imageSourceFrom = ImageSourceFrom.Disk,
    )
}

internal actual suspend fun ColorResource.toFetchResult(options: Options): FetchResult? {
    return FetchResult.OfPainter(
        painter = ColorPainter(Color(getColor(options.androidContext))),
    )
}

internal actual suspend fun FileResource.toFetchResult(options: Options): FetchResult? {
    return FetchResult.OfSource(
        imageSource = options.androidContext.resources.openRawResource(rawResId).toImageSource(),
        imageSourceFrom = ImageSourceFrom.Disk,
    )
}

internal actual suspend fun ImageResource.toFetchResult(options: Options): FetchResult? {
    val drawable = requireNotNull(getDrawable(options.androidContext))
    return FetchResult.OfImage(
        image = drawable.toImage(),
    )
}

package com.seiko.imageloader.component

import android.content.Context
import androidx.compose.ui.unit.Density
import com.seiko.imageloader.util.httpEngine
import io.ktor.client.HttpClient

fun ComponentRegistryBuilder.setupDefaultComponents(
    context: Context,
    density: Density = Density(context),
    maxImageSize: Int = 4096,
    useViewBoundsAsIntrinsicSizeWithSvg: Boolean = true,
    useSvgSizeFirst: Boolean = false,
    httpClient: () -> HttpClient = { HttpClient(httpEngine) },
) {
    setupKtorComponents(httpClient)
    setupBase64Components()
    setupCommonComponents()
    setupJvmComponents()
    setupAndroidComponents(
        context = context,
        density = density,
        useViewBoundsAsIntrinsicSizeWithSvg = useViewBoundsAsIntrinsicSizeWithSvg,
        maxImageSize = maxImageSize,
        useSvgSizeFirst = useSvgSizeFirst,
    )
}

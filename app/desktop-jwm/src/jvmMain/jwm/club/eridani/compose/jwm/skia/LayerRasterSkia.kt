package club.eridani.compose.jwm.skia

import io.github.humbleui.jwm.LayerRaster
import org.jetbrains.skia.*


class LayerRasterSkia : LayerRaster() {
    val colorInfo = ColorInfo(ColorType.BGRA_8888, ColorAlphaType.PREMUL, ColorSpace.sRGB)
    var surface: Surface? = null

    override fun frame() {
        val surface = surface ?: Surface.makeRasterDirect(ImageInfo(colorInfo, width, height), pixelsPtr, rowBytes)

        _window.accept(EventFrameSkia(surface))
        swapBuffers()
    }

    override fun resize(width: Int, height: Int) {
        surface?.close()
        surface = null

        super.resize(width, height)
    }

    override fun close() {
        assert(!isClosed)
        surface?.close()
        surface = null

        super.close()
    }
}
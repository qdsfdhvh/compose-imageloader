package club.eridani.compose.jwm.skia

import io.github.humbleui.jwm.LayerD3D12
import io.github.humbleui.jwm.Window
import org.jetbrains.skia.*


class LayerD3D12Skia : LayerD3D12() {

    private val origin = SurfaceOrigin.TOP_LEFT
    private val colorFormat = SurfaceColorFormat.RGBA_8888
    private val colorSpace = ColorSpace.sRGB
    private val surfaceProps = SurfaceProps(PixelGeometry.RGB_H)

    var context: DirectContext? = null

    override fun attach(window: Window?) {
        super.attach(window)
        context = DirectContext.makeDirect3D(adapterPtr, devicePtr, queuePtr)
    }


    override fun frame() {
        BackendRenderTarget.makeDirect3D(_width, _height, nextDrawableTexturePtr(), format, sampleCount, levelCount).use { rt ->
            Surface.makeFromBackendRenderTarget(context!!, rt, origin, colorFormat, colorSpace, surfaceProps).use { surface ->
                surface ?: error("Failed to create D3D12 layer")
                _window.accept(EventFrameSkia(surface))
                surface.flushAndSubmit()
                swapBuffers()
            }
        }
    }

    override fun resize(width: Int, height: Int) {
        context?.submit(true)
        super.resize(width, height)
    }

    override fun close() {
        assert(!isClosed)
        context?.abandon()
        context?.close()
        super.close()
    }
}
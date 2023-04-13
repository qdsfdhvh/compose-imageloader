package club.eridani.compose.jwm.skia

import io.github.humbleui.jwm.LayerGL
import org.jetbrains.skia.*


class LayerGLSkia : LayerGL() {

    private val samples = 0
    private val stencil = 8
    private val frameBufferID = 0
    private val framebufferFormat = FramebufferFormat.GR_GL_RGBA8
    private val origin = SurfaceOrigin.BOTTOM_LEFT
    private val colorFormat = SurfaceColorFormat.BGRA_8888
    private val colorSpace = ColorSpace.sRGB
    private val surfaceProps = SurfaceProps(PixelGeometry.RGB_H)


    var context: DirectContext? = null
    var renderTarget: BackendRenderTarget? = null
    var surface: Surface? = null


    override fun frame() {
        makeCurrent()
        context = context ?: DirectContext.makeGL()
        renderTarget = renderTarget ?: BackendRenderTarget.makeGL(_width, _height, samples, stencil, frameBufferID, framebufferFormat)
        surface = surface ?: Surface.makeFromBackendRenderTarget(context!!, renderTarget!!, origin, colorFormat, colorSpace, surfaceProps)

        val surface = surface ?: error("Unable to create surface")

        _window.accept(EventFrameSkia(surface))
        surface.flushAndSubmit()
        swapBuffers()
    }

    override fun resize(width: Int, height: Int) {
        surface?.close()
        surface = null

        renderTarget?.close()
        renderTarget = null

//        It depends
//        context?.abandon()
//        context?.close()

//        context = null

        super.resize(width, height)
    }
}
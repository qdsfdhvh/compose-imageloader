package com.seiko.imageloader.svg.internal

import nl.adaptivity.xmlutil.XmlEvent

internal fun parseStartElement(
    tag: String,
    attributes: Array<out XmlEvent.Attribute>,
) {
    when (SvgElement.valueOf(tag)) {
        SvgElement.a -> TODO()
        SvgElement.animate -> TODO()
        SvgElement.animateMotion -> TODO()
        SvgElement.animateTransform -> TODO()
        SvgElement.audio -> TODO()
        SvgElement.canvas -> TODO()
        SvgElement.circle -> TODO()
        SvgElement.clipPath -> TODO()
        SvgElement.defs -> TODO()
        SvgElement.desc -> TODO()
        SvgElement.discard -> TODO()
        SvgElement.ellipse -> TODO()
        SvgElement.feBlend -> TODO()
        SvgElement.feColorMatrix -> TODO()
        SvgElement.feComponentTransfer -> TODO()
        SvgElement.feComposite -> TODO()
        SvgElement.feConvolveMatrix -> TODO()
        SvgElement.feDiffuseLighting -> TODO()
        SvgElement.feDisplacementMap -> TODO()
        SvgElement.feDistantLight -> TODO()
        SvgElement.feDropShadow -> TODO()
        SvgElement.feFlood -> TODO()
        SvgElement.feFuncA -> TODO()
        SvgElement.feFuncB -> TODO()
        SvgElement.feFuncG -> TODO()
        SvgElement.feFuncR -> TODO()
        SvgElement.feGaussianBlur -> TODO()
        SvgElement.feImage -> TODO()
        SvgElement.feMerge -> TODO()
        SvgElement.feMergeNode -> TODO()
        SvgElement.feMorphology -> TODO()
        SvgElement.feOffset -> TODO()
        SvgElement.fePointLight -> TODO()
        SvgElement.feSpecularLighting -> TODO()
        SvgElement.feSpotLight -> TODO()
        SvgElement.feTile -> TODO()
        SvgElement.feTurbulence -> TODO()
        SvgElement.filter -> TODO()
        SvgElement.foreignObject -> TODO()
        SvgElement.g -> TODO()
        SvgElement.iframe -> TODO()
        SvgElement.image -> TODO()
        SvgElement.line -> TODO()
        SvgElement.linearGradient -> TODO()
        SvgElement.marker -> TODO()
        SvgElement.mask -> TODO()
        SvgElement.metadata -> TODO()
        SvgElement.mpath -> TODO()
        SvgElement.path -> TODO()
        SvgElement.pattern -> TODO()
        SvgElement.polygon -> TODO()
        SvgElement.polyline -> TODO()
        SvgElement.radialGradient -> TODO()
        SvgElement.rect -> TODO()
        SvgElement.script -> TODO()
        SvgElement.set -> TODO()
        SvgElement.stop -> TODO()
        SvgElement.style -> TODO()
        SvgElement.svg -> {

        }
        SvgElement.switch -> TODO()
        SvgElement.symbol -> TODO()
        SvgElement.text -> TODO()
        SvgElement.textPath -> TODO()
        SvgElement.title -> TODO()
        SvgElement.tspan -> TODO()
        SvgElement.unknown -> TODO()
        SvgElement.use -> TODO()
        SvgElement.video -> TODO()
        SvgElement.view -> TODO()
    }
}

// https://www.w3.org/TR/SVG2/eltindex.html
@Suppress("EnumEntryName")
private enum class SvgElement {
    a,
    animate,
    animateMotion,
    animateTransform,
    audio,
    canvas,
    circle,
    clipPath,
    defs,
    desc,
    discard,
    ellipse,
    feBlend,
    feColorMatrix,
    feComponentTransfer,
    feComposite,
    feConvolveMatrix,
    feDiffuseLighting,
    feDisplacementMap,
    feDistantLight,
    feDropShadow,
    feFlood,
    feFuncA,
    feFuncB,
    feFuncG,
    feFuncR,
    feGaussianBlur,
    feImage,
    feMerge,
    feMergeNode,
    feMorphology,
    feOffset,
    fePointLight,
    feSpecularLighting,
    feSpotLight,
    feTile,
    feTurbulence,
    filter,
    foreignObject,
    g,
    iframe,
    image,
    line,
    linearGradient,
    marker,
    mask,
    metadata,
    mpath,
    path,
    pattern,
    polygon,
    polyline,
    radialGradient,
    rect,
    script,
    set,
    stop,
    style,
    svg,
    switch,
    symbol,
    text,
    textPath,
    title,
    tspan,
    unknown,
    use,
    video,
    view;
}

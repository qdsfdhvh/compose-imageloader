package com.seiko.imageloader.svg

@Suppress("EnumEntryName")
enum class Unit {
    px,
    em,
    ex,
    `in`,
    cm,
    mm,
    pt,
    pc,
    percent;
}

data class Length(
    val value: Float,
    val unit: Unit = Unit.px,
) {
    companion object {
        val Zero = Length(0f)
        val Percent_100 = Length(100f, Unit.percent)
    }
}

data class SVG(
    val x: Length,
    val y: Length,
    val width: Length,
    val height: Length,
    val version: String,
)

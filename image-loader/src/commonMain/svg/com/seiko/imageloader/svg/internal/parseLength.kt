package com.seiko.imageloader.svg.internal

import com.seiko.imageloader.svg.Length
import com.seiko.imageloader.svg.SVGParseException
import com.seiko.imageloader.svg.Unit

internal fun parseLength(
    value: String,
): Length {
    if (value.isEmpty()) {
        throw SVGParseException("Invalid length value (empty string)")
    }
    var end = value.length
    val unit: Unit
    when {
        value.last() == '%' -> {
            end -= 1
            unit = Unit.percent
        }

        end > 2 && value.last().isLetter()
                && value[value.lastIndex - 1].isLetter() -> {
            end -= 2
            unit = Unit.valueOf(value.substring(startIndex = end))
        }

        else -> {
            unit = Unit.px
        }
    }
    return try {
        Length(
            value = value.substring(0, end).toFloat(),
            unit = unit,
        )
    } catch (e: Exception) {
        throw SVGParseException("Invalid length value: $value", e)
    }
}
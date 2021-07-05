package io.chessy.tool

import java.awt.Color

object ColorFactory {
    fun fromInt(color: Int): Color {
        return Color.getColor(null, color)
    }

    fun rgb(color: Int): RGB {
        return RGB(color shr 16 and 0xFF, (color shr 8) and 0xFF, (color shr 0) and 0xFF)
    }

    fun fromRgb(rgb: RGB): Color {
        return Color(rgb.red, rgb.green, rgb.blue)
    }

    class RGB(val red: Int, val green: Int, val blue: Int)
}
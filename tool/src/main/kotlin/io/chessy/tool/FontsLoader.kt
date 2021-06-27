package io.chessy.tool

import io.chessy.tool.view.PieceView
import java.awt.Font
import java.awt.FontFormatException
import java.awt.GraphicsEnvironment
import java.io.File
import java.io.IOException
import java.net.URL


object FontsLoader {
    fun load() {
        listOf(
            "Gilroy-Black.ttf",
            "Gilroy-BlackItalic.ttf",
            "Gilroy-Bold.ttf",
            "Gilroy-BoldItalic.ttf",
            "Gilroy-Extrabold.ttf",
            "Gilroy-ExtraboldItalic.ttf",
            "Gilroy-Heavy.ttf",
            "Gilroy-HeavyItalic.ttf",
            "Gilroy-Light.ttf",
            "Gilroy-LightItalic.ttf",
            "Gilroy-Medium.ttf",
            "Gilroy-MediumItalic.ttf",
            "Gilroy-Regular.ttf",
            "Gilroy-RegularItalic.ttf",
            "Gilroy-Semibold.ttf",
            "Gilroy-SemiboldItalic.ttf",
            "Gilroy-Thin.ttf",
            "Gilroy-ThinItalic.ttf",
            "Gilroy-UltraLight.ttf",
            "Gilroy-UltraLightItalic.ttf"
        ).forEach { fileName ->
            val url = PieceView::class.java.getResource("/fonts/$fileName")!!
            try {
                val ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
                ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, url.openStream()))
            } catch (e: IOException) {
            } catch (e: FontFormatException) {
            }
        }
    }
}
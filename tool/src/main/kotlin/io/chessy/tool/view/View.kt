package io.chessy.tool.view

import java.awt.Graphics

interface View {
    val x: Int
    val y: Int
    val width: Int
    val height: Int
    fun draw(graphics: Graphics)
}
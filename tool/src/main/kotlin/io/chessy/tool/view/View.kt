package io.chessy.tool.view

import java.awt.Graphics

interface View {
    val x: Int
    val y: Int
    val width: Int
    val height: Int
    fun draw(graphics: Graphics)
    fun copy(x: Int = this.x, y: Int = this.y, width: Int = this.width, height: Int = this.height): View
}

fun View.move(x: Int, y: Int): View {
    return copy(x = x, y = y)
}

fun View.move(x: Float, y: Float): View {
    return copy(x = x.toInt(), y = y.toInt())
}

fun View.resize(width: Int, height: Int): View {
    return copy(width = width, height = height)
}
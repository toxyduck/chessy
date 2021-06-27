package io.chessy.tool.view

import java.awt.Color
import java.awt.Graphics

class RoundBackgroundView(
    private val color: Color,
    private val wrappedView: View,
    private val radius: Int,
    private val paddingHorizontal: Int,
    private val paddingVertical: Int
) : View by wrappedView {

    override val width: Int = wrappedView.width + 2 * paddingHorizontal
    override val height: Int = wrappedView.height + 2 * paddingVertical
    override val x: Int = wrappedView.x
    override val y: Int = wrappedView.y

    override fun draw(graphics: Graphics) {
        graphics.color = color
        graphics.fillRoundRect(x, y, width, height, radius, radius)
        wrappedView.move(wrappedView.x + paddingHorizontal, wrappedView.y + paddingVertical).draw(graphics)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return RoundBackgroundView(color, wrappedView.copy(x, y, width, height), radius, paddingHorizontal, paddingVertical)
    }
}
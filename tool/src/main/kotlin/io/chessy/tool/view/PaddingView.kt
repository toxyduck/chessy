package io.chessy.tool.view

import java.awt.Graphics

class PaddingView(
    private val wrappedView: View,
    private val padding: Int
) : View by wrappedView {

    override fun draw(graphics: Graphics) {
        val resizedView = wrappedView.copy(
            x = wrappedView.x + padding,
            y = wrappedView.y + padding,
            width = wrappedView.width - 2 * padding,
            height = wrappedView.height - 2 * padding
        )
        resizedView.draw(graphics)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return PaddingView(wrappedView.copy(x, y, width, height), padding)
    }

}
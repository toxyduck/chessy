package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics
import kotlin.math.max

class PlayerDetailsView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    private val playerName: String,
    private val playerRating: Int?,
    private val graphicsContext: Graphics
) : ViewGroup<Nothing>() {

    override var height: Int = 0
    private set

    init {
        initViews()
    }

    override fun draw(graphics: Graphics) {
        graphics.color = backgroundColor
        graphics.fillRect(x, y, width, height)
        super.draw(graphics)
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return PlayerDetailsView(x, y, width, playerName, playerRating, graphicsContext)
    }

    private fun initViews() {
        val nameTextView = TextView(
            x = x + PADDING_HORIZONTAL,
            y = y + PADDING_VERTICAL,
            graphicsContext = graphicsContext,
            text = playerName,
            color = textColor,
            font = FONT
        )
        val ratingTextView = TextView(
            x = x + nameTextView.width + PADDING_HORIZONTAL * 2,
            y = y + PADDING_VERTICAL,
            graphicsContext = graphicsContext,
            text = playerRating.toString(),
            color = textColor,
            font = FONT
        )
        height = 2 * PADDING_VERTICAL + max(nameTextView.height, ratingTextView.height)
        addChild(nameTextView)
        addChild(ratingTextView)
    }

    companion object {
        private const val FONT_NAME = "SansSerif"
        private const val FONT_SIZE = 32
        private const val FONT_STYLE = Font.BOLD
        private val FONT = Font(FONT_NAME, FONT_STYLE, FONT_SIZE)
        private const val PADDING_VERTICAL = 64
        private const val PADDING_HORIZONTAL = 32
        private val textColor = Color.decode("#FFFFFF")
        private val backgroundColor = Color.decode("#000000")
    }
}
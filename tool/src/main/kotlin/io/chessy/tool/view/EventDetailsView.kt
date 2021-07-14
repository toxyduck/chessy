package io.chessy.tool.view

import java.awt.Color
import java.awt.Font
import java.awt.Graphics

class EventDetailsView(
    override val x: Int = 0,
    override val y: Int = 0,
    override val width: Int,
    private val event: String,
    private val tournament: String,
    private val date: String,
    private val graphicsContext: Graphics
) : ViewGroup<Nothing>() {

    override val height: Int

    init {
        val viewX = x + PADDING_HORIZONTAL
        val eventView = TextView(viewX, y, graphicsContext, event, textColor, FONT_EVENT)
        val tournamentView = TextView(viewX, eventView.y + eventView.height + PADDING_VERTICAL, graphicsContext, tournament, textColor, FONT_TOURNAMENT)
        val dateView = TextView(viewX, tournamentView.y + tournamentView.height + PADDING_VERTICAL, graphicsContext, date, textColor, FONT_DATE)
        addChild(eventView)
        addChild(tournamentView)
        addChild(dateView)
        height = dateView.y + dateView.height - y + PADDING_VERTICAL
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return EventDetailsView(x, y, width, event, tournament, date, graphicsContext)
    }

    companion object {
        private val FONT_EVENT= Font("Gilroy-Bold", Font.PLAIN, 32)
        private val FONT_TOURNAMENT = Font("Gilroy-Medium", Font.PLAIN, 32)
        private val FONT_DATE = Font("Gilroy-Regular", Font.PLAIN, 32)
        private val textColor = Color.decode("#FFFFFF")
        private const val PADDING_VERTICAL = 32
        private const val PADDING_HORIZONTAL = 32
    }
}
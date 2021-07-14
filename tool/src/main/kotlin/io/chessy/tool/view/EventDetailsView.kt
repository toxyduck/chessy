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
    private val config: Config,
    private val graphicsContext: Graphics
) : ViewGroup<Nothing>() {

    override val height: Int

    init {
        val viewX = x + config.paddingHorizontal
        val eventView = TextView(viewX, y, graphicsContext, event, config.textColor, config.fontEvent)
        val tournamentView = TextView(viewX, eventView.y + eventView.height + config.paddingVertical, graphicsContext, tournament, config.textColor, config.fontTournament)
        val dateView = TextView(viewX, tournamentView.y + tournamentView.height + config.paddingVertical, graphicsContext, date, config.textColor, config.fontDate)
        addChild(eventView)
        addChild(tournamentView)
        addChild(dateView)
        height = dateView.y + dateView.height - y + config.paddingVertical
    }

    override fun copy(x: Int, y: Int, width: Int, height: Int): View {
        return EventDetailsView(x, y, width, event, tournament, date, config, graphicsContext)
    }

    class Config(
        val fontEvent: Font,
        val fontTournament: Font,
        val fontDate: Font,
        val textColor: Color,
        val paddingVertical: Int,
        val paddingHorizontal: Int
    )
}
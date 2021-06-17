package io.chessy.tool.view

import io.chessy.tool.*
import io.chessy.tool.primitive.MovableView
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

class PieceView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val piece: Piece
) : View, MovableView {

    override fun draw(graphics: Graphics) {
        graphics.drawImage(
            piece.icon(),
            x,
            y,
            width,
            height,
            null
        )
    }

    override fun move(x: Int, y: Int): MovableView {
        return PieceView(x, y, width, height, piece)
    }

    private fun Piece.icon(): BufferedImage {
        val cachedImage = piecesCache[this]
        return if (cachedImage != null) {
            cachedImage
        } else {
            val image = ImageIO.read(this.pathToImage())
            piecesCache[this] = image
            image
        }
    }

    private fun Piece.pathToImage(): URL {
        return PieceView::class.java.getResource("/${name()}${side()}.png")!!
    }

    private fun Piece.side(): String {
        return when (side) {
            Side.Black -> "Black"
            Side.White -> "White"
        }
    }

    private fun Piece.name(): String {
        return when (pieceName) {
            PieceName.Pawn -> "Pawn"
            PieceName.Knight -> "Knight"
            PieceName.Bishop -> "Bishop"
            PieceName.Rook -> "Rook"
            PieceName.Queen -> "Queen"
            PieceName.King -> "King"
        }
    }

    private companion object {
        val piecesCache: MutableMap<Piece, BufferedImage> = mutableMapOf()
    }
}
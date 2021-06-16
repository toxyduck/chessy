package io.chessy.tool

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO
import kotlin.math.min

interface MoveDrawer {
    fun addFrameListener(listener: (BufferedImage) -> Unit)

    fun drawMove(board: Board, move: Move)

    fun drawCheckMate(board: Board)

    fun drawBoard(board: Board)

    fun drawCastle(board: Board, kingMove: Move, rookMove: Move)

    fun drawKingAttacked(board: Board, move: Move)
}

class JFrameMoveDrawer(private val width: Int, val height: Int, val fps: Int) : MoveDrawer {
    private var frameListener: ((BufferedImage) -> Unit)? = null
    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2
    private val cellSize: Int = boardSize / 8
    private val cleanBoard: BufferedImage = drawCleanBoard()
    private val piecesIconsCache = mutableMapOf<Piece, BufferedImage>()
    private val frameDuration = 1000 / fps

    override fun addFrameListener(listener: (BufferedImage) -> Unit) {
        frameListener = listener
    }

    override fun drawMove(board: Board, move: Move) {
        drawMoves(board, listOf(move))
    }

    override fun drawCastle(board: Board, kingMove: Move, rookMove: Move) {
        drawMoves(board, listOf(kingMove, rookMove))
    }

    override fun drawKingAttacked(board: Board, move: Move) {
        drawMoves(board, listOf(move), true)
    }

    private fun drawMoves(board: Board, moves: List<Move>, isKingAttacked: Boolean = false) {
        if (frameListener == null) return
        val movedPieces = mutableListOf<Pair<Piece, Move>>()
        var kingSide: Side? = null
        val boardWithoutMovedPiece = board.copy(
            cells = board.cells.map { cellWithPiece ->
                if (isKingAttacked && cellWithPiece.cell == moves.first().from) {
                    kingSide = cellWithPiece.piece!!.side.opposite()
                }
                val move: Move? = moves.find { cellWithPiece.cell == it.from }
                if (move != null) {
                    cellWithPiece.piece?.let {
                        movedPieces.add(it to move)
                    }
                    cellWithPiece.copy(piece = null)
                } else {
                    cellWithPiece
                }
            }
        )
        val image = drawBoardWithPieces(boardWithoutMovedPiece, kingSide)
        val frameCount = MOVE_DURATION / frameDuration
        println("Rendered move ${moves.joinToString()}}")
        for (ix in 0..frameCount) {
            val frame = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
            val g = frame.graphics
            g.drawImage(image, 0, 0, null)
            movedPieces.forEach { (movedPiece, move) ->
                val pieceIcon = movedPiece.icon()
                val leftTopFromX = move.from.x * cellSize + topLeftBoardX
                val leftTopFromY = (7 - move.from.y) * cellSize + topLeftBoardY
                val leftTopToX = move.to.x * cellSize + topLeftBoardX
                val leftTopToY = (7 - move.to.y) * cellSize + topLeftBoardY
                val dx = (leftTopToX - leftTopFromX) / frameCount
                val dy = (leftTopToY - leftTopFromY) / frameCount
                g.drawImage(pieceIcon, leftTopFromX + dx * ix, leftTopFromY + dy * ix, cellSize, cellSize, null)
            }
            frameListener?.invoke(frame)
        }
    }

    private fun drawBoardWithPieces(boardCurrent: Board, kingAttackedSide: Side?): BufferedImage {
        val boardWithPieces = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
        val g = boardWithPieces.graphics
        g.drawImage(cleanBoard, 0, 0, null)
        boardCurrent.cells.forEach {

            if (it.piece != null) {
                val pieceBufferedImage = it.piece.icon()
                val y = 7 - it.cell.y
                kingAttackedSide.let { side ->
                    if (it.piece.pieceName == PieceName.King && it.piece.side == side) {
                        g.color = redColor
                        g.fillRect(
                            topLeftBoardX + cellSize * it.cell.x,
                            topLeftBoardY + cellSize * y,
                            cellSize,
                            cellSize,
                        )
                    }
                }
                g.drawImage(
                    pieceBufferedImage,
                    topLeftBoardX + cellSize * it.cell.x,
                    topLeftBoardY + cellSize * y,
                    cellSize,
                    cellSize,
                    null
                )
            }
        }
        return boardWithPieces
    }

    private fun Piece.icon(): BufferedImage {
        val cachedImage = piecesIconsCache[this]
        return if (cachedImage != null) {
            cachedImage
        } else {
            val image = ImageIO.read(this.pathToImage())
            piecesIconsCache[this] = image
            image
        }
    }

    private fun Piece.pathToImage(): URL {
        return MoveDrawer::class.java.getResource("/${name()}${side()}.png")!!
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

    private fun drawCleanBoard(): BufferedImage {
        val board = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
        val boardView = BoardView(
            x = topLeftBoardX,
            y = topLeftBoardY,
            width = boardSize,
            height = boardSize,
            colorBlack = blackColor,
            colorWhite = whiteColor
        )
        val g = board.graphics
        g.color = backgroundColor
        g.fillRect(0, 0, width, height)
        boardView.draw(g)
        return board
    }

    override fun drawCheckMate(board: Board) {}

    override fun drawBoard(board: Board) {}

    companion object {
        private const val MOVE_DURATION = 500
        private val blackColor = Color.decode("#A57550")
        private val whiteColor = Color.decode("#EBD0A6")
        private val backgroundColor = Color.decode("#E9E6E3")
        private val redColor = Color.decode("#FF0000")
    }
}

interface View {
    val x: Int
    val y: Int
    val width: Int
    val height: Int
    fun draw(graphics: Graphics)
}

interface MovableView<T : View> {
    fun move(x: Int, y: Int): T
}

class BoardView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val colorWhite: Color,
    private val colorBlack: Color
) : View {

    private val cellSize: Int = min(width, height) / 8

    private val cellViews by lazy { views() }

    override fun draw(graphics: Graphics) {
        cellViews.forEach { it.draw(graphics) }
    }

    private fun views(): List<CellView> {
        return (0 until 64).map { ix ->
            val ixX = ix % 8
            val ixY = 7 - (ix / 8)
            val isWhite = isCellWhite(ixX, ixY)
            val cellX = x + cellSize * ixX
            val cellY = y + cellSize * ixY
            CellView(
                x = cellX,
                y = cellY,
                width = cellSize,
                height = cellSize,
                color = if (isWhite) colorWhite else colorBlack
            )
        }
    }

    private fun isCellWhite(x: Int, y: Int): Boolean {
        return ((x + y) % 2) == 0
    }
}

class PieceView (
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val piece: Piece
) : View, MovableView<PieceView> {

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

    override fun move(x: Int, y: Int): PieceView {
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

class CellView(
    override val x: Int,
    override val y: Int,
    override val width: Int,
    override val height: Int,
    private val color: Color
) : View {
    override fun draw(graphics: Graphics) {
        graphics.color = color
        graphics.fillRect(x, y, width, height)
    }
}
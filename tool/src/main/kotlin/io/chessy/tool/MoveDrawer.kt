//package io.chessy.tool
//
//import io.chessy.tool.view.BoardView
//import io.chessy.tool.view.CellView
//import io.chessy.tool.view.PieceView
//import io.chessy.tool.view.View
//import java.awt.Color
//import java.awt.Graphics
//import java.awt.image.BufferedImage
//import java.net.URL
//import javax.imageio.ImageIO
//import kotlin.math.min
//
//interface MoveDrawer {
//    fun addFrameListener(listener: (BufferedImage) -> Unit)
//
//    fun drawMove(board: Board, move: Move)
//
//    fun drawCheckMate(board: Board)
//
//    fun drawBoard(board: Board)
//
//    fun drawCastle(board: Board, kingMove: Move, rookMove: Move)
//
//    fun drawKingAttacked(board: Board, move: Move)
//}
//
//class JFrameMoveDrawer(private val width: Int, val height: Int, val fps: Int) : MoveDrawer {
//    private var frameListener: ((BufferedImage) -> Unit)? = null
//    private val boardSize = width.coerceAtMost(height)
//    private val centerBoardX = width / 2
//    private val centerBoardY = height / 2
//    private val topLeftBoardX = centerBoardX - boardSize / 2
//    private val topLeftBoardY = centerBoardY - boardSize / 2
//    private val cellSize: Int = boardSize / 8
//    private val cleanBoard: BufferedImage = drawCleanBoard()
//    private val piecesIconsCache = mutableMapOf<Piece, BufferedImage>()
//    private val frameDuration = 1000 / fps
//
//    override fun addFrameListener(listener: (BufferedImage) -> Unit) {
//        frameListener = listener
//    }
//
//    override fun drawMove(board: Board, move: Move) {
//        drawMoves(board, listOf(move))
//    }
//
//    override fun drawCastle(board: Board, kingMove: Move, rookMove: Move) {
//        drawMoves(board, listOf(kingMove, rookMove))
//    }
//
//    override fun drawKingAttacked(board: Board, move: Move) {
//        drawMoves(board, listOf(move), true)
//    }
//
//    private fun drawMoves(board: Board, moves: List<Move>, isKingAttacked: Boolean = false) {
//        if (frameListener == null) return
//        println("Rendered move ${moves.joinToString()}}")
//        val movedPieces = mutableListOf<Pair<Piece, Move>>()
//        var kingSide: Side? = null
//        var kingWhite: Cell? = null
//        var kingBlack: Cell? = null
//        val boardWithoutMovedPiece = board.copy(
//            cells = board.cells.map { cellWithPiece ->
//                if (isKingAttacked && cellWithPiece.cell == moves.first().from) {
//                    kingSide = cellWithPiece.piece!!.side.opposite()
//                }
//                if (cellWithPiece.piece != null && cellWithPiece.piece.pieceName == PieceName.King) {
//                    if (cellWithPiece.piece.side == Side.Black) {
//                        kingBlack = cellWithPiece.cell
//                    } else {
//                        kingWhite = cellWithPiece.cell
//                    }
//                }
//                val move: Move? = moves.find { cellWithPiece.cell == it.from }
//                if (move != null) {
//                    cellWithPiece.piece?.let {
//                        movedPieces.add(it to move)
//                    }
//                    cellWithPiece.copy(piece = null)
//                } else {
//                    cellWithPiece
//                }
//            }
//        )
//        val boardWithPiecesView = PieceOnBoardView(
//            topLeftBoardX,
//            topLeftBoardY,
//            cellSize,
//            cellSize,
//            boardWithoutMovedPiece
//        )
//
//        val boardWithPieces = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
//        val boardWithPiecesGraphics = boardWithPieces.graphics
//        boardWithPiecesGraphics.drawImage(cleanBoard, 0, 0, null)
//        if (kingSide != null) {
//            val cellView = if (kingSide == Side.Black) {
//                CellView(
//                    kingBlack!!.x * cellSize + topLeftBoardX,
//                    kingBlack!!.y * cellSize + topLeftBoardY,
//                    cellSize,
//                    cellSize,
//                    redColor
//                )
//            } else {
//                CellView(
//                    kingWhite!!.x * cellSize + topLeftBoardX,
//                    kingWhite!!.y * cellSize + topLeftBoardY,
//                    cellSize,
//                    cellSize,
//                    redColor
//                )
//            }
//            cellView.draw(graphics = boardWithPiecesGraphics)
//        }
//        boardWithPiecesView.draw(graphics = boardWithPiecesGraphics)
//        val movedPiece = movedPieces.map {
//            PieceView(
//                it.second.from.x * cellSize + topLeftBoardX,
//                it.second.from.y * cellSize + topLeftBoardY,
//                cellSize,
//                cellSize,
//                it.first
//            )
//        }
//
//        val animatorPieceMove = AnimatorPieceMove(
//            pieceViews = movedPiece,
//            interpolator = InterpolatorCube(),
//            duration = MOVE_DURATION,
//            frameListener = frameListener!!,
//            coordinatesTo = movedPieces.map {
//                Pair(
//                    it.second.to.x * cellSize + topLeftBoardX,
//                    it.second.to.y * cellSize + topLeftBoardY
//                )
//            }
//        )
//        boardWithPiecesGraphics.drawImage(boardWithPieces, 0, 0, null)
//        animatorPieceMove.startAnimator(boardWithPieces)
//    }
//
//    private fun drawBoardWithPieces(boardCurrent: Board, kingAttackedSide: Side?): BufferedImage {
//        val boardWithPieces = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
//        val g = boardWithPieces.graphics
//        g.drawImage(cleanBoard, 0, 0, null)
//        boardCurrent.cells.forEach {
//            if (it.piece != null) {
//                val pieceBufferedImage = it.piece.icon()
//                val y = 7 - it.cell.y
//                kingAttackedSide.let { side ->
//                    if (it.piece.pieceName == PieceName.King && it.piece.side == side) {
//                        g.color = redColor
//                        g.fillRect(
//                            topLeftBoardX + cellSize * it.cell.x,
//                            topLeftBoardY + cellSize * y,
//                            cellSize,
//                            cellSize,
//                        )
//                    }
//                }
//                g.drawImage(
//                    pieceBufferedImage,
//                    topLeftBoardX + cellSize * it.cell.x,
//                    topLeftBoardY + cellSize * y,
//                    cellSize,
//                    cellSize,
//                    null
//                )
//            }
//        }
//        return boardWithPieces
//    }
//
//    private fun Piece.icon(): BufferedImage {
//        val cachedImage = piecesIconsCache[this]
//        return if (cachedImage != null) {
//            cachedImage
//        } else {
//            val image = ImageIO.read(this.pathToImage())
//            piecesIconsCache[this] = image
//            image
//        }
//    }
//
//    private fun Piece.pathToImage(): URL {
//        return MoveDrawer::class.java.getResource("/${name()}${side()}.png")!!
//    }
//
//    private fun Piece.side(): String {
//        return when (side) {
//            Side.Black -> "Black"
//            Side.White -> "White"
//        }
//    }
//
//    private fun Piece.name(): String {
//        return when (pieceName) {
//            PieceName.Pawn -> "Pawn"
//            PieceName.Knight -> "Knight"
//            PieceName.Bishop -> "Bishop"
//            PieceName.Rook -> "Rook"
//            PieceName.Queen -> "Queen"
//            PieceName.King -> "King"
//        }
//    }
//
//    private fun drawCleanBoard(): BufferedImage {
//        val board = BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR)
//        val boardView = BoardView(
//            x = topLeftBoardX,
//            y = topLeftBoardY,
//            width = boardSize,
//            height = boardSize,
//            colorBlack = blackColor,
//            colorWhite = whiteColor
//        )
//        val g = board.graphics
//        g.color = backgroundColor
//        g.fillRect(0, 0, width, height)
//        boardView.draw(g)
//        return board
//    }
//
//    override fun drawCheckMate(board: Board) {}
//
//    override fun drawBoard(board: Board) {}
//
//    companion object {
//        private const val MOVE_DURATION = 500
//        private val blackColor = Color.decode("#A57550")
//        private val whiteColor = Color.decode("#EBD0A6")
//        private val backgroundColor = Color.decode("#E9E6E3")
//        private val redColor = Color.decode("#FF0000")
//    }
//}
//
//interface Animator {
//    fun startAnimator(bufferedImage: BufferedImage)
//}
//
//interface Interpolator {
//    fun interpolate(t: Float): Float
//}
//
//class AnimatorPieceMove(
//    private val pieceViews: List<PieceView>,
//    private val coordinatesTo: List<Pair<Int, Int>>,
//    private val interpolator: Interpolator,
//    private val duration: Int,
//    private val frameListener: (BufferedImage) -> Unit
//) : Animator {
//    override fun startAnimator(bufferedImage: BufferedImage) {
//        val frameCount = (duration / 16)
//        for (ix in 0..frameCount) {
//            val frame = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_3BYTE_BGR)
//            val g = frame.graphics
//            pieceViews.forEachIndexed { pieceViewsIx, it ->
//                g.drawImage(bufferedImage, 0, 0, null)
//                val dx =
//                    ((coordinatesTo[pieceViewsIx].first - it.x) * interpolator.interpolate(ix / frameCount.toFloat()))
//                        .toInt()
//                val dy =
//                    ((coordinatesTo[pieceViewsIx].second - it.y) * interpolator.interpolate(ix / frameCount.toFloat()))
//                        .toInt()
//                it.move(it.x + dx, it.y + dy).draw(g)
//            }
//            frameListener.invoke(frame)
//        }
//    }
//}
//
//class InterpolatorCube : Interpolator {
//    override fun interpolate(t: Float): Float {
//        return t * t * t
//    }
//}
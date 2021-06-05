import java.awt.Color
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

interface MoveDrawer {
    fun addFrameListener(listener: (BufferedImage) -> Unit)

    fun drawMove(board: Board, move: Move)

    fun drawCheckMate(board: Board)

    fun drawBoard(board: Board)
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
        if (frameListener == null) return
        var movedPiece: Piece? = null
        val boardWithoutMovedPiece = board.copy(
            cells = board.cells.map { cellWithPiece ->
                if (cellWithPiece.cell == move.from) {
                    movedPiece = cellWithPiece.piece
                    cellWithPiece.copy(piece = null)
                } else {
                    cellWithPiece
                }
            }
        )
        val image = drawBoardWithPieces(boardWithoutMovedPiece)
        val movedPieceFinal = movedPiece
        if (movedPieceFinal != null) {
            val pieceIcon = movedPieceFinal.icon()
            val frameCount = MOVE_DURATION / frameDuration
            val leftTopFromX = move.from.x * cellSize + topLeftBoardX
            val leftTopFromY = (7-move.from.y) * cellSize + topLeftBoardY
            val leftTopToX = move.to.x * cellSize + topLeftBoardX
            val leftTopToY = (7-move.to.y) * cellSize + topLeftBoardY
            val dx = (leftTopToX - leftTopFromX)/frameCount
            val dy = (leftTopToY - leftTopFromY)/frameCount
            for (ix in 0..frameCount) {
                val frame = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
                val g = frame.graphics
                g.drawImage(image, 0, 0, null)
                g.drawImage(pieceIcon, leftTopFromX + dx*ix , leftTopFromY + dy*ix, cellSize, cellSize, null)

                print("Rendered move from = ${move.from} to = ${move.to}")

                frameListener?.invoke(frame)
            }
        }
    }

    override fun drawCheckMate(board: Board) {
        TODO("Not yet implemented")
    }

    override fun drawBoard(board: Board) {
    }

    private fun drawBoardWithPieces(boardCurrent: Board): BufferedImage {
        val boardWithPieces = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = boardWithPieces.graphics
        g.drawImage(cleanBoard, 0, 0, null)
        boardCurrent.cells.forEach {
            if (it.piece != null) {
                val pieceBufferedImage = it.piece.icon()
                val y = 7 - it.cell.y
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
        return MoveDrawer::class.java.getResource("${name()}${side()}.png")!!
    }

    private fun Piece.side(): String {
        return when(side) {
            Side.Black -> "Black"
            Side.White -> "White"
        }
    }

    private fun Piece.name(): String {
        return when(pieceName) {
            PieceName.Pawn -> "Pawn"
            PieceName.Knight -> "Knight"
            PieceName.Bishop -> "Bishop"
            PieceName.Rock -> "Rock"
            PieceName.Queen -> "Queen"
            PieceName.King -> "King"
        }
    }

   private fun drawCleanBoard(): BufferedImage {
        val board = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = board.graphics
        realBoard.cells.forEach {
            val y = 7 - it.cell.y
            g.color = if (it.cell.isBlack()) Color.BLUE else Color.LIGHT_GRAY
            g.fillRect(topLeftBoardX + cellSize * it.cell.x, topLeftBoardY + cellSize * y, cellSize, cellSize)
        }
        return board
    }

    companion object {
        private const val MOVE_DURATION = 500
    }
}
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import javax.imageio.ImageIO

interface MoveDrawer {
    fun addFrameListener(listener: (BufferedImage) -> Unit)

    fun drawMove(board: Board, move: Move)

    fun drawCheckMate(board: Board)

    fun drawBoard(board: Board)
}

class JFrameMoveDrawer(private val width: Int, val height: Int) : MoveDrawer {
    private var frameListener: ((BufferedImage) -> Unit)? = null
    private val boardSize = width.coerceAtMost(height)
    private val centerBoardX = width / 2
    private val centerBoardY = height / 2
    private val topLeftBoardX = centerBoardX - boardSize / 2
    private val topLeftBoardY = centerBoardY - boardSize / 2
    private val cellSize: Int = boardSize / 8
    private val board: BufferedImage = drawCleanBoard()
    private val piecesIconsCache = mutableMapOf<Piece, BufferedImage>()

    override fun addFrameListener(listener: (BufferedImage) -> Unit) {
        frameListener = listener
    }

    override fun drawMove(board: Board, move: Move) {
        TODO("Not yet implemented")
    }

    override fun drawCheckMate(board: Board) {
        TODO("Not yet implemented")
    }

    override fun drawBoard(board: Board) {

    }

    fun drawBoardWithPieces(): BufferedImage {
        val boardWithPieces = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = boardWithPieces.graphics
        g.drawImage(board, 0, 0, null)
        realBoard.cells.forEach {
            if (it.piece != null) {
                val cachedImage = piecesIconsCache[it.piece]
                val pieceBufferedImage = if (cachedImage != null) {
                    cachedImage
                } else {
                    val image = ImageIO.read(it.piece.pathToImage())
                    piecesIconsCache[it.piece] = image
                    image
                }
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
}
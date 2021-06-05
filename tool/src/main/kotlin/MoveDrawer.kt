import java.awt.Color
import java.awt.image.BufferedImage

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
    private val board: BufferedImage = drawCleanBoard()
    private val cellSize: Int = boardSize / 8


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

     private fun drawCleanBoard(): BufferedImage {
        val board = BufferedImage(width, height, BufferedImage.TYPE_INT_RGB)
        val g = board.graphics
        realBoard.cells.forEach {
            val y = 7 - it.cell.y
            g.color = if (it.cell.isBlack()) Color.BLACK else Color.WHITE
            g.fillRect(topLeftBoardX + cellSize * it.cell.x, topLeftBoardY + cellSize * y, cellSize, cellSize)
        }
        return board
    }
}
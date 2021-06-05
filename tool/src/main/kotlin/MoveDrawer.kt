import java.awt.image.BufferedImage

interface MoveDrawer {
    fun addFrameListener(listener: (BufferedImage) -> Unit)

    fun drawMove(board:Board, move: Move)

    fun drawCheckMate(board:Board)

    fun drawBoard(board:Board)
}
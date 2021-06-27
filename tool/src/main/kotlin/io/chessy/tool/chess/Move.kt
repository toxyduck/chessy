package io.chessy.tool.chess


data class Move(
    val from: Cell,
    val to: Cell,
    val isCastleMove: Boolean = false,
    val isMateMove: Boolean = false,
    val isKingAttacked: Boolean = false,
    val piece: Piece,
    val promotionPiece: Piece? = null
) {

    val side = piece.side

    fun rookCastleMove(): Move? {
        if (!isCastleMove) return null
        return if (from.y == 7) {
            if (to.x >= 4) {
                Move(Cell(7, 7), Cell(5, 7), piece = Piece(PieceName.Rook, piece.side))
            } else {
                Move(Cell(0, 7), Cell(3, 7), piece = Piece(PieceName.Rook, piece.side))
            }
        } else {
            if (to.x >= 4) {
                Move(Cell(7, 0), Cell(5, 0), piece = Piece(PieceName.Rook, piece.side))
            } else {
                Move(Cell(0, 0), Cell(3, 0), piece = Piece(PieceName.Rook, piece.side))
            }
        }
    }
}

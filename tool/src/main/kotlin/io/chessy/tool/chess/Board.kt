package io.chessy.tool.chess

data class Board(val cells: List<CellWithPiece>) {

    fun mutate(move: Move): Board {
        val castleMove : Move? =  if (move.isCastleMove) { move.rookCastleMove() } else null
        val castlePiece: Piece? = castleMove?.piece
        var piece = move.piece
        var capturedCell: Cell? = null
        if (piece.pieceName == PieceName.Pawn) {
            val isCapture = (move.from.x - move.to.x) != 0
            if (isCapture) {
                val capturedPiece = cells.find { it.cell == move.to }?.piece
                if (capturedPiece == null) {
                    capturedCell = Cell(move.to.x, move.from.y)
                }
            }
            if (move.promotionPiece != null) {
                piece = move.promotionPiece
            }
        }
        return this.copy(cells = this.cells.map {
            when (it.cell) {
                capturedCell -> it.copy(piece = null)
                move.from -> it.copy(piece = null)
                move.to -> it.copy(piece = piece)
                castleMove?.from -> it.copy(piece = null)
                castleMove?.to -> it.copy(piece = castlePiece)
                else -> it
            }
        })
    }

    fun filter(filter: List<Cell>): Board {
        return copy(cells = this.cells.map {
            if (filter.contains(it.cell)) it.copy(piece = null) else it
        })
    }

    fun kingCell(side: Side): Cell {
        return cells.find { it.piece?.pieceName == PieceName.King && it.piece.side == side }?.cell!!
    }
}

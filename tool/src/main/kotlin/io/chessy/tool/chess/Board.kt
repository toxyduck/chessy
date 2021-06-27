package io.chessy.tool.chess

data class Board(val cells: List<CellWithPiece>) {

    fun mutate(move: Move): Board {
        var piece = cells.find { it.cell == move.from }?.piece
        var capturedCell: Cell? = null
        if (piece != null && piece.pieceName == PieceName.Pawn) {
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
        val mutated = this.copy(cells = this.cells.map {
            when (it.cell) {
                capturedCell -> it.copy(piece = null)
                move.from -> it.copy(piece = null)
                move.to -> it.copy(piece = piece)
                else -> it
            }
        })
        return if (move.isCastleMove) move.rookCastleMove()?.let { mutated.mutate(it) } ?: mutated else mutated
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

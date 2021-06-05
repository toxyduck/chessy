
val whitePawn = Piece(PieceName.Pawn, Side.White)
val blackPawn = Piece(PieceName.Pawn, Side.Black)
val whiteKnight = Piece(PieceName.Knight, Side.White)
val blackKnight = Piece(PieceName.Knight, Side.Black)
val whiteBishop = Piece(PieceName.Bishop, Side.White)
val blackBishop = Piece(PieceName.Bishop, Side.Black)
val whiteRock = Piece(PieceName.Rock, Side.White)
val blackRock = Piece(PieceName.Rock, Side.Black)
val whiteQueen = Piece(PieceName.Queen, Side.White)
val blackQueen = Piece(PieceName.Queen, Side.Black)
val whiteKing = Piece(PieceName.King, Side.White)
val blackKing = Piece(PieceName.King, Side.Black)

private val realInitialBoardFigures = listOf(
    blackRock, blackKnight, blackBishop, blackQueen, blackKing, blackBishop, blackKnight, blackRock,
    blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn, blackPawn,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    null, null, null, null, null, null, null, null,
    whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn, whitePawn,
    whiteRock, whiteKnight, whiteBishop, whiteQueen, whiteKing, whiteBishop, whiteKnight, whiteRock
)

private val realCells = (0 until 64).map { ix ->
    val x = ix % 8
    val y = 7 - (ix / 8)
    Cell(x, y)
}

val realBoard = Board(
    realInitialBoardFigures.zip(realCells) { piece: Piece?, cell ->
        CellWithPiece(cell, piece)
    }
)

val whitePawn = Figure(FigureName.Pawn, Side.White)
val blackPawn = Figure(FigureName.Pawn, Side.Black)
val whiteKnight = Figure(FigureName.Knight, Side.White)
val blackKnight = Figure(FigureName.Knight, Side.Black)
val whiteBishop = Figure(FigureName.Bishop, Side.White)
val blackBishop = Figure(FigureName.Bishop, Side.Black)
val whiteRock = Figure(FigureName.Rock, Side.White)
val blackRock = Figure(FigureName.Rock, Side.Black)
val whiteQueen = Figure(FigureName.Queen, Side.White)
val blackQueen = Figure(FigureName.Queen, Side.Black)
val whiteKing = Figure(FigureName.King, Side.White)
val blackKing = Figure(FigureName.King, Side.Black)

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
    realInitialBoardFigures.zip(realCells) { figure: Figure?, cell ->
        CellWithFigure(cell, figure)
    }
)
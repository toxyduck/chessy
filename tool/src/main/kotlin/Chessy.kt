import java.lang.Exception
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder

class Chessy {

    private val drawer = JFrameMoveDrawer(1080, 1920, 60)
    private val videoMaker = FFmpegVideoMaker(1080, 1920, 60)

    init {
        drawer.addFrameListener(videoMaker::addFrame)
    }

    fun fromPgn(pgnString: String) {
        val pgn = PgnHolder("")
        pgn.loadPgn(pgnString)
        if (pgn.games.isNotEmpty()) {
            val game = pgn.games.first()
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            for (move in moves) {
                board.doMove(move)
            }
            val blackPlayer = game.blackPlayer.name
            val whitePlayer = game.whitePlayer.name
            val where = game.round.event.name
            val initialState = realBoard
            val domainMoves = moves.mapIndexed { ix, move ->
                val from = Cell.fromNotation(move.from.value()) ?: throw Exception()
                val to = Cell.fromNotation(move.to.value()) ?: throw Exception()
                Move(from, to, board.backup[ix].isCastleMove)
            }
            val domainGame = Game(
                whitePlayer,
                blackPlayer,
                where,
                initialState,
                domainMoves
            )
            videoMaker.startRecord()
            var currentBoard = initialState
            domainMoves.forEach { move ->
                if (move.isCastleMove) {
                    move.rockCastleMove()?.let { rockMove ->
                        drawer.drawCastle(currentBoard, move, rockMove)
                    }
                } else {
                    drawer.drawMove(currentBoard, move)
                }
                currentBoard = currentBoard.mutate(move)
            }
//            var currentBoard = initialState
//            domainMoves.subList(0, 10).forEach {
//                drawer.drawMove(currentBoard, it)
//                currentBoard = currentBoard.mutate(it)
//            }
            videoMaker.endRecord()
        }
    }
}
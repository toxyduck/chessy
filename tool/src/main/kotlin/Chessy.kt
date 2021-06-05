import java.lang.Exception
import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.pgn.PgnHolder

class Chessy {
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
            val domainMoves = moves.map {
                val from = Cell.fromNotation(it.from.value()) ?: throw Exception()
                val to = Cell.fromNotation(it.to.value()) ?: throw Exception()
                Move(from, to)
            }
            val domainGame = Game(
                whitePlayer,
                blackPlayer,
                where,
                initialState,
                domainMoves
            )
        }
    }
}
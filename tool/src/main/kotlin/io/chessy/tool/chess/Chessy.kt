package io.chessy.tool.chess

import com.github.bhlangonijr.chesslib.Board
import com.github.bhlangonijr.chesslib.game.GameResult
import com.github.bhlangonijr.chesslib.pgn.PgnHolder
import io.chessy.tool.FFmpegVideoMaker
import io.chessy.tool.controller.Controller
import io.chessy.tool.rootViewConfig
import io.chessy.tool.view.PieceView
import io.chessy.tool.view.RootView
import java.net.URL


class Chessy(
    private val width: Int,
    private val height: Int,
    private val fps: Int,
    private val whitePlayer: Player?,
    private val blackPlayer: Player?,
    private val event: String?,
    private val tournament: String?,
    private val date: String?,
    private val pgnString: String,
    private val config: RootView.Config
) {

    fun renderTo(outputFilePath: String) {
        val videoMaker = FFmpegVideoMaker(width, height, fps, outputFilePath)
        val pgn = PgnHolder("")
        pgn.loadPgn(pgnString)
        if (pgn.games.isNotEmpty()) {
            val game = pgn.games.first()
            game.loadMoveText()
            val moves = game.halfMoves
            val board = Board()
            val mateMoves = mutableListOf<Pair<Boolean, Boolean>>()
            val piecesMoved = mutableListOf<Pair<Piece, PromotionPiece>>()
            for (move in moves) {
                piecesMoved.add(Pair(board.getPiece(move.from).toDomainPiece()!!, move.promotion.toDomainPiece()))
                board.doMove(move)
                mateMoves.add(Pair(board.isMated, board.isKingAttacked))
            }
            val blackPlayerName = game.blackPlayer.name
            val whitePlayerName = game.whitePlayer.name
            val where = game.round.event.name
            val tournamentName = game.round.event.site
            val initialState = realBoard
            val domainMoves = moves.mapIndexed { ix, move ->
                val from = Cell.fromNotation(move.from.value()) ?: throw Exception()
                val to = Cell.fromNotation(move.to.value()) ?: throw Exception()
                Move(
                    from,
                    to,
                    board.backup[ix].isCastleMove,
                    mateMoves[ix].first,
                    mateMoves[ix].second,
                    piecesMoved[ix].first,
                    piecesMoved[ix].second
                )
            }

            val domainGame = Game(
                whitePlayer = whitePlayer ?: Player(whitePlayerName, game.whitePlayer.elo, null),
                blackPlayer = blackPlayer ?: Player(blackPlayerName, game.blackPlayer.elo, null),
                event ?: where,
                tournament ?: tournamentName,
                date ?: game.date,
                initialState,
                domainMoves,
                game.result.toResult()
            )
            videoMaker.startRecord()
            val controller = Controller(domainGame, domainMoves, width, height, fps, config) {
                videoMaker.addFrame(it)
            }
            controller.startRender()
            videoMaker.endRecord()
        }
    }

    fun GameResult.toResult(): RootView.GameResult {
        return when(this) {
            GameResult.WHITE_WON -> RootView.GameResult.WHITE_WIN
            GameResult.BLACK_WON -> RootView.GameResult.BLACK_WIN
            GameResult.DRAW -> RootView.GameResult.DRAW
            GameResult.ONGOING -> RootView.GameResult.DRAW
        }
    }

    class Builder(private val width: Int, private val height: Int, private val fps: Int = 60, private val config: RootView.Config = rootViewConfig) {
        private var whitePlayer: Player? = null
        private var blackPlayer: Player? = null
        private var event: String? = null
        private var tournament: String? = null
        private var date: String? = null

        fun whitePlayer(player: Player): Builder {
            whitePlayer = player
            return this
        }

        fun blackPlayer(player: Player): Builder {
            blackPlayer = player
            return this
        }

        fun event(event: String): Builder {
            this.event = event
            return this
        }

        fun tournament(tournament: String): Builder {
            this.tournament = tournament
            return this
        }

        fun date(date: String): Builder {
            this.date = date
            return this
        }

        fun build(pgn: String): Chessy {
            return Chessy(
                width = width,
                height = height,
                fps = fps,
                whitePlayer = whitePlayer,
                blackPlayer = blackPlayer,
                event = event,
                tournament = tournament,
                date = date,
                pgnString = pgn,
                rootViewConfig
            )
        }
    }

}
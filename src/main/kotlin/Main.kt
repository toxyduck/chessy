package io.chessy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.chessy.tool.chess.Chessy

fun main() {
    val httpAsync = "https://lichess.org/game/export/RHxgDZ8q"
        .httpGet()
        .responseString { _, _, result ->
            when (result) {
                is Result.Success -> Chessy().fromPgn(result.get())
                is Result.Failure -> {
                    val ex = result.getException()
                    println(ex)
                }
            }
        }
    httpAsync.join()
}
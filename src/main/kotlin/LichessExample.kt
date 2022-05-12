package io.chessy

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import io.chessy.tool.Chessy

class LichessExample {
    fun run() {
        val httpAsync = "https://lichess.org/game/export/oifURCPW"
            .httpGet()
            .responseString { _, _, result ->
                when (result) {
                    is Result.Success -> {
                        Chessy.Builder(width = 1080, height = 1920, fps = 60)
                            .build(result.get())
                            .renderTo("/tmp/out.mp4")
                    }
                    is Result.Failure -> {
                        val ex = result.getException()
                        println(ex)
                    }
                }
            }
        httpAsync.join()
    }
}
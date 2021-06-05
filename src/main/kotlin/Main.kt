import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result

fun main() {
    val httpAsync = "https://lichess.org/game/export/ji0P8SHM"
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
    val mp4SavePath = "/tmp/img.mp4"
    val width = 1920
    val height = 1080
    createMp4(mp4SavePath, width, height)
}
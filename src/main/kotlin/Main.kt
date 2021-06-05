import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import java.io.File
import javax.imageio.ImageIO

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
}
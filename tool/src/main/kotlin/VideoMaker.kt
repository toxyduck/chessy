import org.bytedeco.ffmpeg.global.avcodec
import org.bytedeco.ffmpeg.global.avutil
import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.javacv.Java2DFrameConverter
import java.awt.image.BufferedImage
import java.io.File
import java.io.InputStream
import javax.imageio.ImageIO

interface VideoMaker {
    fun startRecord()

    fun addFrame(bufferedImage: BufferedImage)

    fun endRecord(): InputStream
}

fun createMp4(videoSavePath: String, width: Int, height: Int) {
    val recorder = FFmpegFrameRecorder(videoSavePath, width, height)
    recorder.videoCodec = avcodec.AV_CODEC_ID_NONE
    recorder.pixelFormat = avutil.AV_PIX_FMT_YUV420P
    recorder.frameRate = 60.0
    recorder.format = "mp4"
    try {
        val board = ImageIO.read(File("/tmp/test/dos.jpg"))
        val pawn = ImageIO.read(File("/tmp/test/lw.png"))
        val w: Int = Math.max(board.width, pawn.width)
        val h: Int = Math.max(board.height, pawn.height)
        val moveDistance = h / 60
        recorder.start()
        val converter = Java2DFrameConverter()
        var currentPosition = 0
        for (j in 0..60) {
            val combined = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
            val simpleImage = BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR)
            val g = combined.graphics
            g.drawImage(board, 0, 0, null)
            g.drawImage(pawn, 200, currentPosition, 130, 130, null)
            currentPosition += moveDistance
            simpleImage.graphics.drawImage(combined, 0, 0, null)
//            ImageIO.write(simpleImage, "PNG", File("/tmp/test", "simpleImage.png"))
//            ImageIO.write(combined, "PNG", File("/tmp/test", "combined.png"))
            recorder.record(converter.getFrame(simpleImage))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        recorder.stop()
        recorder.release()
    }
}
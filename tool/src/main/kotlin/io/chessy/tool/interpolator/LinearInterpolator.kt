package io.chessy.tool.interpolator

object LinearInterpolator : Interpolator {
    override fun interpolate(frame: Int, duration: Int): Float {
        return (frame.toFloat()) / duration
    }
}
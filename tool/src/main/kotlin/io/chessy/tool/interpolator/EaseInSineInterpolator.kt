package io.chessy.tool.interpolator

import java.lang.Math.*

object EaseInSineInterpolator : Interpolator {
    override fun interpolate(frame: Int, duration: Int): Float {
        val x = LinearInterpolator.interpolate(frame, duration)
        return kotlin.math.sin((x * PI) / 2).toFloat()
    }
}
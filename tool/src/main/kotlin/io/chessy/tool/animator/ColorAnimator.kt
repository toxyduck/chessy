package io.chessy.tool.animator

import io.chessy.tool.ColorFactory
import io.chessy.tool.interpolator.Interpolator
import io.chessy.tool.view.View
import java.awt.Color

class ColorAnimator<T : View>(
    interpolator: Interpolator,
    animatedView: T,
    duration: Int,
    private val fromColor: Int,
    private val toColor: Int,
    private val recolor: (Color, T) -> T
) : Animator<T>(interpolator, animatedView, duration) {
    override fun animate(animatedView: T, condition: Float): View {
        val rgbFrom = ColorFactory.rgb(fromColor)
        val rgbTo = ColorFactory.rgb(toColor)
        val newRgb = ColorFactory.RGB(
            red = computeColor(rgbFrom.red, rgbTo.red, condition),
            green = computeColor(rgbFrom.green, rgbTo.green, condition),
            blue = computeColor(rgbFrom.blue, rgbTo.blue, condition)
        )
        return recolor(ColorFactory.fromRgb(newRgb), animatedView)
    }

    private fun computeColor(from: Int, to: Int, condition: Float): Int {
        return (from + (to - from) * condition).toInt()
    }
}
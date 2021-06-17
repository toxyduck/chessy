package io.chessy.tool.animator

import io.chessy.tool.interpolator.Interpolator
import io.chessy.tool.view.View
import java.awt.Graphics

abstract class Animator<T : View>(
    private val interpolator: Interpolator,
    private val animatedView: T,
    private val duration: Int
) : View by animatedView {

    private var currentFrame = 0
    private var isFinish = false

    abstract fun animate(animatedView: T, condition: Float): View

    override fun draw(graphics: Graphics) {
        val condition = interpolator.interpolate(currentFrame, duration)
        val newView = animate(animatedView, condition)
        newView.draw(graphics)
        if (currentFrame != duration - 1) {
            currentFrame++
        } else {
            isFinish = true
        }
    }

    fun isFinish(): Boolean = isFinish

}
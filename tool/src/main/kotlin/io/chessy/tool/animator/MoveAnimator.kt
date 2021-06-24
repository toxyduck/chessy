package io.chessy.tool.animator

import io.chessy.tool.interpolator.Interpolator
import io.chessy.tool.primitive.Point
import io.chessy.tool.view.View
import io.chessy.tool.view.move

class MoveAnimator(
    interpolator: Interpolator,
    animatedView: View,
    duration: Int,
    private val target: Point
) : Animator<View>(interpolator, animatedView, duration) {

    override fun animate(animatedView: View, condition: Float): View {
        val diffPoint = Point(target.x - animatedView.x, target.y - animatedView.y)
        return animatedView.move(diffPoint.x * condition + animatedView.x, diffPoint.y * condition + animatedView.y)
    }
}
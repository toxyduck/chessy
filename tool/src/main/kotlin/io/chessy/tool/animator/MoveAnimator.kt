package io.chessy.tool.animator

import io.chessy.tool.interpolator.Interpolator
import io.chessy.tool.primitive.MovableView
import io.chessy.tool.primitive.Point
import io.chessy.tool.view.View

class MoveAnimator(
    interpolator: Interpolator,
    animatedView: MovableView,
    duration: Int,
    private val target: Point
) : Animator<MovableView>(interpolator, animatedView, duration) {

    override fun animate(animatedView: MovableView, condition: Float): View {
        val diffPoint = Point(target.x - animatedView.x, target.y - animatedView.y)
        return animatedView.move(diffPoint.x * condition, diffPoint.y * condition)
    }

}
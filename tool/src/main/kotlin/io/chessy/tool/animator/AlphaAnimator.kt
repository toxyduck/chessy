package io.chessy.tool.animator

import io.chessy.tool.interpolator.Interpolator
import io.chessy.tool.view.AlphaView
import io.chessy.tool.view.View

class AlphaAnimator(
    interpolator: Interpolator,
    animatedView: AlphaView,
    duration: Int,
) : Animator<AlphaView>(interpolator, animatedView, duration) {
    override fun animate(animatedView: AlphaView, condition: Float): View {
        return animatedView.transparent(condition)
    }
}
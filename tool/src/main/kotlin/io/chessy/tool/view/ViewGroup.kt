package io.chessy.tool.view

import io.chessy.tool.primitive.ActionView
import io.chessy.tool.primitive.Finishable
import io.chessy.tool.primitive.Timer
import java.awt.Graphics

abstract class ViewGroup<T> : ActionView<T> {

    private val childes: MutableList<ScopedView> = mutableListOf()

    private val timers: MutableList<Timer> = mutableListOf()

    protected open fun obtainAction(action: T) {
        // No actions
    }

    override fun isFinish(): Boolean {
        return (childes.map { it.view }.filterIsInstance<Finishable>() + timers)
            .fold(true) { acc, finishable ->
                acc && finishable.isFinish()
            }
    }

    override fun draw(graphics: Graphics) {
        timers.forEach { it.tick() }
        childes.forEach { it.view.draw(graphics) }
    }

    override fun produceAction(action: T) {
        removeActionScope()
        timers.clear()
        obtainAction(action)
    }

    protected fun addChild(view: View) {
        childes.add(ScopedView(view, ViewScope.Forever))
    }

    protected fun addChildOneAction(view: View) {
        childes.add(ScopedView(view, ViewScope.Action))
    }

    protected fun addChildesOneAction(vararg views: View) {
        views.forEach { view -> childes.add(ScopedView(view, ViewScope.Action)) }
    }

    protected fun finishActionAfterFrames(framesCount: Int) {
        timers.add(Timer(framesCount))
    }

    private fun removeActionScope() {
        childes.removeAll { it.scope == ViewScope.Action }
    }

    private enum class ViewScope {
        Forever, Action
    }

    private class ScopedView(val view: View, val scope: ViewScope)

}
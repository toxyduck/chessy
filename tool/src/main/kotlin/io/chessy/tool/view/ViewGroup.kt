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

    protected fun addChild(view: View, tag: String? = null, z: Int? = null) {
        childes.add(ScopedView(view, ViewScope.Forever, z ?: DEFAULT_Z, tag))
        sortByZ()
    }

    protected fun addChildOneAction(view: View, z: Int? = null) {
        childes.add(ScopedView(view, ViewScope.Action, z ?: DEFAULT_Z))
        sortByZ()
    }

    protected fun addChildesOneAction(vararg views: View) {
        views.forEach { view -> childes.add(ScopedView(view, ViewScope.Action, DEFAULT_Z)) }
        sortByZ()
    }

    protected fun finishActionAfterFrames(framesCount: Int) {
        timers.add(Timer(framesCount))
    }

    protected fun removeView(tag: String): View? {
        val removedView = childes.find { it.tag == tag }
        if (removedView != null) {
            childes.removeAll { it.tag == tag }
        }
        return removedView?.view
    }

    protected inline fun <reified T : View> removeViewSafe(tag: String): T? {
        val removedView = removeView(tag)
        return if (removedView is T) removedView else null
    }

    private fun sortByZ() {
        childes.sortBy { it.z }
    }

    private fun removeActionScope() {
        childes.removeAll { it.scope == ViewScope.Action }
    }

    private enum class ViewScope {
        Forever, Action
    }

    private class ScopedView(val view: View, val scope: ViewScope, val z: Int, val tag: String? = null)

    companion object {
        private const val DEFAULT_Z = 1
    }
}
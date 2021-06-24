package io.chessy.tool.view

import io.chessy.tool.primitive.ActionView
import io.chessy.tool.primitive.Finishable
import java.awt.Graphics

abstract class ViewGroup<T> : ActionView<T> {

    private var childes: MutableList<ScopedView> = mutableListOf()

    open fun obtainAction(action: T) {
        // No actions
    }

    override fun isFinish(): Boolean {
        return childes.fold(true) { acc, scopedView ->
            val view = scopedView.view
            val isFinishable = view is Finishable
            acc && (!isFinishable || (view as Finishable).isFinish())
        }
    }

    override fun draw(graphics: Graphics) {
        childes.forEach { it.view.draw(graphics) }
    }

    override fun produceAction(action: T) {
        removeActionScope()
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

    private fun removeActionScope() {
        childes.removeAll { it.scope == ViewScope.Action }
    }

    private enum class ViewScope {
        Forever, Action
    }

    private class ScopedView(val view: View, val scope: ViewScope)

}
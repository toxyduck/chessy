package io.chessy.tool.primitive

import io.chessy.tool.view.View

interface Finishable {
    fun isFinish(): Boolean
}

interface ActionConsumer<T> : Finishable {
    fun produceAction(action: T)
}

interface ActionView<T> : View, ActionConsumer<T>
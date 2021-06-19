package io.chessy.tool.primitive

import io.chessy.tool.view.View

interface Movable<T> {
    fun move(x: Int, y: Int): T
    fun move(x: Float, y: Float): T {
        return move(x.toInt(), y.toInt())
    }
}

interface MovableView : View, Movable<MovableView>
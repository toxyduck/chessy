package io.chessy.tool.primitive

class Timer(private val tickCount: Int) : Finishable {

    private var currentTick = 0

    fun tick() {
        if (currentTick < tickCount) {
            currentTick++
        }
    }

    override fun isFinish(): Boolean {
        return currentTick >= tickCount
    }

}
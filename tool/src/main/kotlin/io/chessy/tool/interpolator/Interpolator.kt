package io.chessy.tool.interpolator

interface Interpolator {
    fun interpolate(frame: Int, duration: Int): Float
}
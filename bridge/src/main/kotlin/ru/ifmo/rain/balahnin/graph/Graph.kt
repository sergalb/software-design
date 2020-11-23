package ru.ifmo.rain.balahnin.graph

import ru.ifmo.rain.balahnin.drawing.DrawingApi

abstract class Graph(val drawingApi: DrawingApi) {
    /**
     * Bridge to drawing api
     */
    abstract fun drawGraph()
}

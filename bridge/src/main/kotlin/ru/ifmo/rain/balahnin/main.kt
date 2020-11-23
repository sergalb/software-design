package ru.ifmo.rain.balahnin

import ru.ifmo.rain.balahnin.drawing.AWTDrawGraph
import ru.ifmo.rain.balahnin.drawing.JavaFxDrawGraph
import ru.ifmo.rain.balahnin.graph.AdjacencyMatrixGraph
import ru.ifmo.rain.balahnin.graph.EdgesListGraph

fun main(args: Array<String>) {

    val drawingApi = when (args[0]) {
        "--awt" -> AWTDrawGraph()
        "--fx"  -> JavaFxDrawGraph()
        else  -> throw IllegalArgumentException("first argument should specify drawing api. " +
                "Found ${args[0]}. Expected \"--awt\" or \"--fx\" ")
    }
    val graph = when (args[1]) {
        "--adjacency" -> AdjacencyMatrixGraph(drawingApi)
        "--edgesList" -> EdgesListGraph(drawingApi)
        else        -> throw IllegalArgumentException("first argument should specify drawing api." +
                " Found ${args[0]}. Expected \"--adjacency\" or \"--edgesList\" ")
    }
    graph.drawGraph()
}
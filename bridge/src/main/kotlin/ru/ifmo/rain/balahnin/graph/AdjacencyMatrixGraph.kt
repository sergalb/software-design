package ru.ifmo.rain.balahnin.graph

import ru.ifmo.rain.balahnin.drawing.DrawingApi

class AdjacencyMatrixGraph(drawingApi: DrawingApi) : Graph(drawingApi) {
    val graph: List<List<Int>>
    val size: Int
    init {
        println(
"""enter adjacency matrix in format 
n
a_1_e1 a_2_e2 ...
a_2_e1 a_2_e2 ...
...
a_n_e1 a_2_e2 ...
where 'n' - number of vertexes
line a_i - is list of neighbours for vertex a_i.
if in line i exist number j, then expected number i in line j
for example
4
1 2
0 2 3
0 1
1 
present a graph with 4 vertexes and edges 
(0 <-> 1); (0 <-> 2); (1 <-> 2); (1 <-> 3);"""
        )
        val tmpGraph: MutableList<List<Int>> = ArrayList()
        size = readLine()?.toInt() ?: 0
        for (i in 0 until size) {
            tmpGraph.add(readLine()?.split(" ")
                ?.asSequence()
                ?.map { it.toInt() }
                ?.toList()
                ?: emptyList())
        }
        graph = tmpGraph
    }

    override fun drawGraph() {
        drawingApi.prepareArea(size)
        for ((ind, vertex) in graph.withIndex()) {
            for (neighbour in vertex) {
                if (ind < neighbour) {
                    drawingApi.drawEdge(ind, neighbour)
                }
            }
        }
    }

}
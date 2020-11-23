package ru.ifmo.rain.balahnin.graph

import ru.ifmo.rain.balahnin.drawing.DrawingApi

class EdgesListGraph(drawingApi: DrawingApi) : Graph(drawingApi) {
    val graph: Set<Pair<Int, Int>>
    val size: Int
    init {
        println(
"""enter edges list in format 
n
ei1, ej1
ei2, ej2
...
ein, ejn
where 'n' - number of vertexes
line k include presentation of one edge between (il <-> jl)
for example
4
0 1
0 2
1 2
1 3
present a graph with 4 vertexes and edges
(0 <-> 1); (0 <-> 2); (1 <-> 2); (1 <-> 3);
loop are restricted.
each edge should included one time
"""
        )
        size = readLine()?.toInt() ?: 0
        val tmpGraph: MutableSet<Pair<Int, Int>> = HashSet()
        for (i in 0 until size) {
            tmpGraph.add(readLine()!!.split(" ")
                .map { it.toInt() }
                .sorted()
                .zipWithNext()[0])
        }
        graph = tmpGraph
    }

    override fun drawGraph() {
        drawingApi.prepareArea(size)
        for (edge in graph) {
            drawingApi.drawEdge(edge.first, edge.second)
        }
    }

}
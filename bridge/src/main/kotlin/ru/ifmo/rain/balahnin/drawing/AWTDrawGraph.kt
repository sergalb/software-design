package ru.ifmo.rain.balahnin.drawing

import java.awt.Frame
import java.awt.Graphics
import java.awt.Graphics2D
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.awt.geom.Ellipse2D
import java.awt.geom.Line2D
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.system.exitProcess

class AWTDrawGraph() : Frame("draw graph by awt"), DrawingApi {
    data class Line(val x1: Double, val y1: Double, val x2: Double, val y2: Double)

    private var partitionAngle = 0.0
    private val lines: MutableList<Line> = ArrayList()
    private val vertexRadius = 5.0
    private val screenSize = Toolkit.getDefaultToolkit().screenSize
    private val radius = min(screenSize.width, screenSize.height) * 0.4

    override fun paint(gg: Graphics) {
        val pointsCount = 10
        val g = gg as Graphics2D
        for (ind in 0..pointsCount) {
            val (x, y) = calculateCoordinate(ind)

            g.fill(Ellipse2D.Double(x - vertexRadius, y - vertexRadius, vertexRadius, vertexRadius))
        }
        for (line in lines) {
            drawLine(g, line)
        }
    }

    private fun drawLine(graphics2D: Graphics2D, line: Line) {
        graphics2D.draw(Line2D.Double(line.x1, line.y1, line.x2, line.y2))
    }

    override fun prepareArea(pointsCount: Int) {
        partitionAngle = 2 * Math.PI / pointsCount
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(we: WindowEvent) {
                exitProcess(0)
            }
        })
        setSize(screenSize.width, screenSize.height)
        isVisible = true
    }

    override fun drawEdge(firstInd: Int, secondInd: Int) {
        val (x1, y1) = calculateCoordinate(firstInd)
        val (x2, y2) = calculateCoordinate(secondInd)
        lines.add(Line(x1, y1, x2, y2))
        repaint()
    }

    private fun calculateCoordinate(ind: Int): Pair<Double, Double> {
        val angle = partitionAngle * ind
        return sin(angle) * radius + screenSize.width / 2 to cos(angle) * radius + screenSize.height / 2
    }

}
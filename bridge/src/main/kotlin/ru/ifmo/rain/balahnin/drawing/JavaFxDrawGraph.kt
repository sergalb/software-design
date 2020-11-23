package ru.ifmo.rain.balahnin.drawing

import javafx.application.Application
import javafx.application.Platform
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.shape.Circle
import javafx.scene.shape.Line
import javafx.stage.Stage
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.math.cos
import kotlin.math.sin

private val instanceLock: ReentrantLock = ReentrantLock()
private val instanceInitialized: Condition = instanceLock.newCondition()
private var javaFXApplicationInstance: JavaFxDrawGraphImpl? = null

class JavaFxDrawGraph : DrawingApi {
    override fun prepareArea(pointsCount: Int) {
        Thread { Application.launch(JavaFxDrawGraphImpl::class.java) }.start()
        while (javaFXApplicationInstance == null) {
            instanceLock.withLock {
                instanceInitialized.await()
            }
        }
        Platform.runLater {
            javaFXApplicationInstance!!.prepareArea(pointsCount)
        }
    }

    override fun drawEdge(firstInd: Int, secondInd: Int) =
        Platform.runLater {
            javaFXApplicationInstance!!.drawEdge(firstInd, secondInd)
        }

}

class JavaFxDrawGraphImpl : Application(), DrawingApi {
    lateinit var primaryStage: Stage
    lateinit var root: Group
    var partitionAngle = 0.0
    val radius = 95f

    init {
        instanceLock.withLock {
            javaFXApplicationInstance = this
            instanceInitialized.signalAll()
        }

    }

    override fun start(primaryStage: Stage) {
        this.primaryStage = primaryStage
    }

    override fun prepareArea(pointsCount: Int) {
        primaryStage.title = "Drawing graph"
        partitionAngle = 2 * Math.PI / pointsCount
        root = Group()
        for (ind in 0..pointsCount) {
            val (x, y) = calculateCoordinate(ind)
            val circle = Circle(x, y, 2.0)
            root.children.add(circle)
        }
        primaryStage.scene = Scene(root, 1000.0, 500.0)
        primaryStage.show()

    }

    fun calculateCoordinate(ind: Int): Pair<Double, Double> {
        val angle = partitionAngle * ind
        return sin(angle) * radius + 500 to cos(angle) * radius + 250
    }

    override fun drawEdge(firstInd: Int, secondInd: Int) {
        val (x1, y1) = calculateCoordinate(firstInd)
        val (x2, y2) = calculateCoordinate(secondInd)
        val edge = Line(x1, y1, x2, y2)
        root.children.add(edge)
    }
}
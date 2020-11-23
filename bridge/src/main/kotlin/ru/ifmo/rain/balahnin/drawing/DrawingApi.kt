package ru.ifmo.rain.balahnin.drawing

public interface DrawingApi {
    fun prepareArea(pointsCount: Int);
    fun drawEdge(firstInd: Int, secondInd: Int)
}
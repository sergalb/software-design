package ru.ifmo.rain.balahnin.mvcexample.dao

import ru.ifmo.rain.balahnin.mvcexample.model.PointStatus
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoPoint

interface ToDoPointDao {
    fun addToDoPoint(toDoPoint: ToDoPoint): Int
    fun deleteTodoPoint(id: Int): Boolean
}
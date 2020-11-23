package ru.ifmo.rain.balahnin.mvcexample.dao

import ru.ifmo.rain.balahnin.mvcexample.model.ToDoList

interface ToDoListsDao {
    fun getToDoLists(): List<ToDoList>
    fun addToDoList(toDoList: ToDoList): Int
    fun deleteTodoList(id: Int): Boolean
}
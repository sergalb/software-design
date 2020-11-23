package ru.ifmo.rain.balahnin.mvcexample.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoPointDao
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoList
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoPoint

@Controller
open class ToDoPointController(val toDoPointDao: ToDoPointDao) {
    @PostMapping("/add-todo-point/{parentId}")
    fun addTodoList(@ModelAttribute("todo-point") toDoPoint: ToDoPoint, @PathVariable parentId: Int): String {
        toDoPoint.parentId = parentId
        toDoPointDao.addToDoPoint(toDoPoint)
        return "redirect:/get-todo-lists"
    }

    @DeleteMapping("/delete-todo-point/{id}")
    fun deleteTodoList(@PathVariable id: Int): String {
        toDoPointDao.deleteTodoPoint(id)
        return "redirect:/get-todo-lists"
    }
}
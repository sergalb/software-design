package ru.ifmo.rain.balahnin.mvcexample.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.*
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoListsDao
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoListsDaoImpl
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoList
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoPoint

@Controller
class ToDoListsController(val toDoListsDao: ToDoListsDao) {
    @PostMapping("/add-todo-list")
    fun addTodoList(@ModelAttribute("todo-list") toDoList: ToDoList): String {
        toDoListsDao.addToDoList(toDoList)
        return "redirect:/get-todo-lists"
    }

    @GetMapping("/get-todo-lists")
    fun getTodoLists(map: ModelMap): String {
        map.addAttribute("todolists", toDoListsDao.getToDoLists())
        map.addAttribute("todolist", ToDoList())
        map.addAttribute("todopoint", ToDoPoint())
        return "index"
    }

    @DeleteMapping("/delete-todo-list/{id}")
    fun deleteTodoList(@PathVariable id: Int): String {
        toDoListsDao.deleteTodoList(id)
        return "redirect:/get-todo-lists"
    }
}
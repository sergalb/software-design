package ru.ifmo.rain.balahnin.mvcexample.model

import lombok.Data
import org.simpleflatmapper.map.annotation.Key

@Data
data class ToDoList(
        @Key
        var id: Int = 0,
        var name: String = "",
        var points: MutableList<ToDoPoint> = mutableListOf()
)


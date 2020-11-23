package ru.ifmo.rain.balahnin.mvcexample.model

import lombok.Data
import org.simpleflatmapper.map.annotation.Key

@Data
data class ToDoPoint(
        @Key
        var id: Int = 0,
        val name: String = "",
        var status: PointStatus = PointStatus.InProgress,
        var parentId: Int = 0
)

enum class PointStatus() { InProgress, Done, Canceled, Failed }
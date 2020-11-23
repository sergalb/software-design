package ru.ifmo.rain.balahnin.mvcexample.dao

import org.springframework.jdbc.core.support.JdbcDaoSupport
import ru.ifmo.rain.balahnin.mvcexample.model.PointStatus
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoPoint
import javax.sql.DataSource

class ToDoPointDaoImpl(dataSource: DataSource) : JdbcDaoSupport(), ToDoPointDao {
    init {
        setDataSource(dataSource)
    }
    override fun addToDoPoint(toDoPoint: ToDoPoint): Int {
        val sql = "INSERT INTO ToDoPoints (NAME, status, toDoListId) VALUES (?, ? ,?)"
        return jdbcTemplate!!.update(sql, toDoPoint.name,toDoPoint.status, toDoPoint.parentId)
    }

    override fun deleteTodoPoint(id: Int): Boolean {
        val sql = "DELETE FROM ToDoPoints where id = ?"
        return jdbcTemplate!!.update(sql, id) == 1
    }
}
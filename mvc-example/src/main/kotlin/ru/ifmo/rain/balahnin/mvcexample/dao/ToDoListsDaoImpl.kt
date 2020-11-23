package ru.ifmo.rain.balahnin.mvcexample.dao

import org.jooq.lambda.tuple.Tuple2
import org.simpleflatmapper.jdbc.spring.JdbcTemplateMapperFactory
import org.simpleflatmapper.jdbc.spring.ResultSetExtractorImpl
import org.simpleflatmapper.util.TypeReference
import org.springframework.jdbc.core.query
import org.springframework.jdbc.core.support.JdbcDaoSupport
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoList
import ru.ifmo.rain.balahnin.mvcexample.model.ToDoPoint
import javax.sql.DataSource

class ToDoListsDaoImpl(dataSource: DataSource) : JdbcDaoSupport(), ToDoListsDao {
    init {
        setDataSource(dataSource)
    }

    override fun getToDoLists(): List<ToDoList> {
        val resultSetExtractor: ResultSetExtractorImpl<Tuple2<ToDoList, MutableList<ToDoPoint>>>? = JdbcTemplateMapperFactory.newInstance()
                .addKeys("id")
                .newResultSetExtractor(object : TypeReference<Tuple2<ToDoList, MutableList<ToDoPoint>>>() {})
        val sqlGetLists = "SELECT ToDoLists.id as id, ToDoLists.name as name, TDP.id, TDP.name, status FROM ToDoLists left join ToDoPoints TDP on ToDoLists.id = TDP.toDoListId order by ToDoLists.id"

        val points = resultSetExtractor?.let { jdbcTemplate!!.query(sqlGetLists, it) }.orEmpty()

        return points.asSequence().map {
            it.v1.points = it.v2
            return@map it.v1
        }.toList()
    }

    override fun addToDoList(toDoList: ToDoList): Int {
        val sql = "INSERT INTO ToDoLists (NAME) VALUES (?)"
        return jdbcTemplate!!.update(sql, toDoList.name)
    }

    override fun deleteTodoList(id: Int): Boolean {
        val deleteList = "DELETE FROM ToDoLists where id = ?"
        val deletePoints = "DELETE FROM ToDoPoints where toDoListId = ?"
        return jdbcTemplate!!.update(deleteList, id) == 1 && jdbcTemplate!!.update(deletePoints, id) == 1

    }


}
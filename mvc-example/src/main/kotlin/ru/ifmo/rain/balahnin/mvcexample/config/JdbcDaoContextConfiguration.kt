package ru.ifmo.rain.balahnin.mvcexample.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DriverManagerDataSource
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoListsDao
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoListsDaoImpl
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoPointDao
import ru.ifmo.rain.balahnin.mvcexample.dao.ToDoPointDaoImpl
import javax.sql.DataSource

@Configuration
open class JdbcDaoContextConfiguration {

    @Bean
    open fun todoListsJdbcDao(dataSource: DataSource): ToDoListsDao = ToDoListsDaoImpl(dataSource)

    @Bean
    open fun todoPointJdbcDao(dataSource: DataSource): ToDoPointDao = ToDoPointDaoImpl(dataSource)

    @Bean
    open fun dataSource(): DataSource {
        val dataSource = DriverManagerDataSource()
        dataSource.setDriverClassName("org.sqlite.JDBC")
        dataSource.url = "jdbc:sqlite:todo-lists.db"
        dataSource.username = ""
        dataSource.password = ""
        return dataSource
    }
}
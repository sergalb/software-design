package ru.ifmo.rain.balahnin.mvcexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
open class MvcExampleApplication

fun main(args: Array<String>) {
    runApplication<MvcExampleApplication>(*args)
}

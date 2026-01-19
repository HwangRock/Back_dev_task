package com.example.task

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients(basePackages = ["com.example.task.utils.client"])
class TaskApplication

fun main(args: Array<String>) {
	runApplication<TaskApplication>(*args)
}

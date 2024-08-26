package org.websitedashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class ServerDashboardApplication

fun main(args: Array<String>) {
    runApplication<ServerDashboardApplication>(*args)
}

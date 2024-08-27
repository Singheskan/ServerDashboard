package org.websitedashboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Main application class for the Server Dashboard.
 * It serves as the entry point of the Spring Boot application.
 */
@SpringBootApplication
@EnableScheduling
class ServerDashboardApplication

/**
 * Main method to run the Spring Boot application.
 * @param args - Command line arguments passed to the application.
 */
fun main(args: Array<String>) {
    runApplication<ServerDashboardApplication>(*args)
}

package dev.pritam.blocking

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.runApplication

@ConfigurationProperties
@SpringBootApplication
class KotlinSbBlockingApplication

fun main(args: Array<String>) {
    runApplication<KotlinSbBlockingApplication>(*args) {
    }
}

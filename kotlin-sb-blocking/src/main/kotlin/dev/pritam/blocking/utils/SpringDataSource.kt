package dev.pritam.blocking.utils

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration


@ConfigurationProperties(prefix="db")
@Configuration
data class SpringDataSource(
    var url: String?,
    var user: String?,
    var pass: String?
)
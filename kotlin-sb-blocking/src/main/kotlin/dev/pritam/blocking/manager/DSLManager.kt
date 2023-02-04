package dev.pritam.blocking.manager

import dev.pritam.blocking.utils.SpringDataSource
import org.jooq.DSLContext
import org.jooq.impl.DSL
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component

@Component
@Scope("singleton")
class DSLManager(
   private val config : SpringDataSource
) {
    fun getDSL(): DSLContext {
        return DSL.using(
            config.url,
            config.user,
            config.pass
        ).dsl()
    }
}
package dev.pritam.blocking.service

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductsServiceTest {

    private val service = ProductsService()

    @Test
    fun getAllProducts() {
        assertEquals(service.getAllProducts().size, 0)
    }
}

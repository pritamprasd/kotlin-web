package dev.pritam.blocking.controller

import dev.pritam.blocking.api.ProductsApi
import dev.pritam.blocking.model.Product
import dev.pritam.blocking.service.ProductsService
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid

@RestController
class ProductsController(
    private val service: ProductsService
) : ProductsApi {
    override fun listProducts(@Parameter(description = "How many items to return at one time (max 100)") @Valid @RequestParam(value = "limit", required = false) limit: kotlin.Int?): ResponseEntity<List<Product>> {
        return ResponseEntity.ok(service.getAllProducts())
    }

    override fun showProductById(productId: String): ResponseEntity<Product> {
        return ResponseEntity.ok(service.getProduct(productId.toInt()))
    }
}

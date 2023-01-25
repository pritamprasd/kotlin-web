package dev.pritam.blocking.controller

import dev.pritam.blocking.generated.api.ProductsApi
import dev.pritam.blocking.generated.model.Product
import io.swagger.v3.oas.annotations.Parameter
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
class ProductsController : ProductsApi {
    override fun listProducts(@Parameter(description = "How many items to return at one time (max 100)") @Valid @RequestParam(value = "limit", required = false) limit: kotlin.Int?): ResponseEntity<List<Product>> {
        return ResponseEntity(HttpStatus.OK)
    }
}

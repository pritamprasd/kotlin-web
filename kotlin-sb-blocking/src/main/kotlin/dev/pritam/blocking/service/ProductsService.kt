package dev.pritam.blocking.service

import dev.pritam.blocking.Tables
import dev.pritam.blocking.manager.DSLManager
import dev.pritam.blocking.model.Product
import dev.pritam.blocking.utils.toResponseModel
import org.springframework.stereotype.Service

@Service
class ProductsService(
    val dsl: DSLManager
) {
    fun getAllProducts(): List<Product> {
        return dsl.getDSL().selectFrom(Tables.PRODUCT).map{
            it.toResponseModel()
        }.toList()
    }

    fun getProduct(id: Int): Product? {
        return dsl.getDSL().selectFrom(Tables.PRODUCT).where(Tables.PRODUCT.ID.eq(id))
            .fetchOne()?.toResponseModel();
    }
}

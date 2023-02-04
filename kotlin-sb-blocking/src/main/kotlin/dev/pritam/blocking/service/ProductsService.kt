package dev.pritam.blocking.service

import dev.pritam.blocking.Tables.*
import dev.pritam.blocking.manager.DSLManager
import dev.pritam.blocking.model.Product
import dev.pritam.blocking.utils.toResponseModel
import org.springframework.stereotype.Service

@Service
class ProductsService(
    val dsl: DSLManager
) {
    fun getAllProducts(): List<Product> {
        return dsl.getDSL().selectFrom(PRODUCT).map{
            it.toResponseModel()
        }.toList()
    }

    fun getProduct(id: Int): Product? {
        return dsl.getDSL().selectFrom(PRODUCT).where(PRODUCT.ID.eq(id))
            .fetchOne()?.toResponseModel();
    }

    fun createProduct(product: Product) {
        dsl.getDSL().insertInto(PRODUCT, PRODUCT.NAME, PRODUCT.TAG).values(
            product.name, product.tag
        ).execute()
    }

    fun deleteProduct(productId: Int) {
        dsl.getDSL().delete(PRODUCT).where(PRODUCT.ID.eq(productId)).execute()
    }


}

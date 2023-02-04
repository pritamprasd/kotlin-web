package dev.pritam.blocking.utils

import dev.pritam.blocking.model.Product
import dev.pritam.blocking.tables.records.ProductRecord

fun ProductRecord.toResponseModel(): Product{
    return Product(
        id= this.id.toLong(),
        name=this.name,
        tag=this.tag
    )
}
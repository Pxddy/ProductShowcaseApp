package de.ph.productshowcaseapp.data

import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.remote.ProductDto
import de.ph.productshowcaseapp.domain.Product
import javax.inject.Inject

class ProductMapper @Inject constructor() {

    fun toDomain(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            // NOTE: The DummyJSON API does not provide any currency information.
            // For this demo app, we are assuming that all prices are in EUR.
            // In a real-world application, the API would explicitly provide the currency,
            // or a conversion would need to be performed here based on a base currency (e.g., USD).
            price = entity.price,
            currencyCode = "EUR",
            rating = entity.rating,
            category = entity.category,
            thumbnail = entity.thumbnail,
            images = entity.images
        )
    }

    fun toEntity(dto: ProductDto): ProductEntity {
        return ProductEntity(
            id = dto.id,
            title = dto.title,
            description = dto.description,
            price = dto.price,
            rating = dto.rating,
            category = dto.category,
            thumbnail = dto.thumbnail,
            images = dto.images
        )
    }

    fun toEntityList(dtos: List<ProductDto>): List<ProductEntity> {
        return dtos.map { toEntity(it) }
    }
}

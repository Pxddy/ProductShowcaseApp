package de.ph.productshowcaseapp.data

import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.remote.ProductDto
import de.ph.productshowcaseapp.domain.Product

class ProductMapper {

    fun toDomain(entity: ProductEntity): Product {
        return Product(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            price = entity.price,
            rating = entity.rating,
            category = entity.category,
            thumbnail = entity.thumbnail,
            images = entity.images.split(",") // Simple CSV split
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
            images = dto.images.joinToString(",") // Simple CSV join
        )
    }

    fun toEntityList(dtos: List<ProductDto>): List<ProductEntity> {
        return dtos.map { toEntity(it) }
    }
}

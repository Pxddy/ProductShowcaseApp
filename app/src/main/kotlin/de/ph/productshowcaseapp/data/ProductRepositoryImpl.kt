package de.ph.productshowcaseapp.data

import de.ph.productshowcaseapp.data.local.ProductDao
import de.ph.productshowcaseapp.data.remote.ProductApiService
import de.ph.productshowcaseapp.domain.Product
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val apiService: ProductApiService,
    private val productDao: ProductDao
) : ProductRepository {

    override fun getProducts(): Flow<List<Product>> {
        // TODO: Implement data orchestration from API and DB with caching logic
        return productDao.getProducts().map {
            it.map { entity ->
                Product(
                    id = entity.id,
                    title = entity.title,
                    description = entity.description,
                    price = entity.price,
                    rating = entity.rating,
                    category = entity.category,
                    thumbnail = entity.thumbnail,
                    images = emptyList() // TODO: Proper conversion from String to List<String>
                )
            }
        }
    }

    override fun getProductById(id: Int): Flow<Product> {
        // TODO: Implement data orchestration from API and DB with caching logic
        return productDao.getProductById(id).map {
            Product(
                id = it.id,
                title = it.title,
                description = it.description,
                price = it.price,
                rating = it.rating,
                category = it.category,
                thumbnail = it.thumbnail,
                images = emptyList() // TODO: Proper conversion from String to List<String>
            )
        }
    }
}

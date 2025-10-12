package de.ph.productshowcaseapp.domain

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<List<Product>>
    fun getProductById(id: Int): Flow<Product>
}

package de.ph.productshowcaseapp.domain

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProducts(): Flow<PagingData<Product>>
    fun getProductById(id: Int): Flow<Product>
}

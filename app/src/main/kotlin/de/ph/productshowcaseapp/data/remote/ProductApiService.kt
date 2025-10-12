package de.ph.productshowcaseapp.data.remote

interface ProductApiService {
    suspend fun getProducts(limit: Int, skip: Int): ProductListResponse
}

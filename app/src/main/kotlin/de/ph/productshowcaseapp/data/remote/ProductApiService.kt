package de.ph.productshowcaseapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface ProductApiService {

    @GET("products")
    suspend fun getProducts(
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("limit") limit: Int,
        @Query("skip") skip: Int
    ): ProductListResponse
}

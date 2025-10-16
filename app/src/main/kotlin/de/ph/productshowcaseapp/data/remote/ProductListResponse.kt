package de.ph.productshowcaseapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductListResponse(
    @SerialName("products")
    val products: List<ProductDto>,

    @SerialName("total")
    val total: Int,

    @SerialName("skip")
    val skip: Int,

    @SerialName("limit")
    val limit: Int
)

package de.ph.productshowcaseapp.data.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ProductDto(
    @SerialName("id")
    val id: Int,

    @SerialName("title")
    val title: String,

    @SerialName("description")
    val description: String,

    @SerialName("price")
    val price: Double,

    @SerialName("rating")
    val rating: Double,

    @SerialName("category")
    val category: String,

    @SerialName("thumbnail")
    val thumbnail: String,

    @SerialName("images")
    val images: List<String>
)

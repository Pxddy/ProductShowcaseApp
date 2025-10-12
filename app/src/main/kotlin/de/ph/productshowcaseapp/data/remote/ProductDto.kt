package de.ph.productshowcaseapp.data.remote

data class ProductDto(
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val category: String,
    val thumbnail: String,
    val images: List<String>
)

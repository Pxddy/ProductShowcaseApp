package de.ph.productshowcaseapp.ui.feature.detail

data class ProductDetail(
    val title: String,
    val description: String,
    val price: Double,
    val currencyCode: String,
    val rating: Double,
    val images: List<String>,
)

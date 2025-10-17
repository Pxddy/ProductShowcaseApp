package de.ph.productshowcaseapp.ui.feature.list

data class ProductListItem(
    val thumbnail: String,
    val title: String,
    val price: Double,
    val currency: String,
    val category: String
)

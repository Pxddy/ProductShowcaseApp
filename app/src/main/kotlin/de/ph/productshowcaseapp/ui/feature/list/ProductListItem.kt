package de.ph.productshowcaseapp.ui.feature.list

data class ProductListItem(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val price: Double,
    val currencyCode: String,
    val category: String
)

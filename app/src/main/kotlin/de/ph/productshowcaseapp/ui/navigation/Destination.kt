package de.ph.productshowcaseapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Destination {
    @Serializable
    data object ProductList : Destination

    @Serializable
    data class ProductDetail(val productId: Int) : Destination
}
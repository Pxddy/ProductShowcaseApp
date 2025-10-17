package de.ph.productshowcaseapp.ui.feature.list

import de.ph.productshowcaseapp.domain.Product
import javax.inject.Inject

class ProductListItemMapper @Inject constructor() {

    fun toUi(domain: Product): ProductListItem {
        return ProductListItem(
            thumbnail = domain.thumbnail,
            title = domain.title,
            price = domain.price,
            currency = "EUR", // Assuming EUR for now
            category = domain.category
        )
    }
}

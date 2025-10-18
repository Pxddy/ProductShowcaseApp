package de.ph.productshowcaseapp.ui.feature.list

import de.ph.productshowcaseapp.domain.Product
import javax.inject.Inject

class ProductListItemMapper @Inject constructor() {

    fun toUi(domain: Product): ProductListItem {
        return ProductListItem(
            id = domain.id,
            thumbnail = domain.thumbnail,
            title = domain.title,
            price = domain.price,
            currencyCode = domain.currencyCode,
            category = domain.category
        )
    }
}

package de.ph.productshowcaseapp.ui.feature.detail

import de.ph.productshowcaseapp.domain.Product
import javax.inject.Inject

class ProductDetailMapper @Inject constructor() {

    fun toProductDetail(product: Product): ProductDetail = with(product) {
        ProductDetail(
            title = title,
            description = description,
            price = price,
            currencyCode = currencyCode,
            rating = rating,
            images = images,
        )
    }
}

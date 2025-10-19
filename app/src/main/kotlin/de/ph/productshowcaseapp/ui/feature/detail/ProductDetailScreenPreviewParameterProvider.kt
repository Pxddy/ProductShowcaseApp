package de.ph.productshowcaseapp.ui.feature.detail

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class ProductDetailScreenPreviewParameterProvider : PreviewParameterProvider<ProductDetailUiState> {
    override val values: Sequence<ProductDetailUiState> = sequenceOf(
        ProductDetailUiState.Loading,
        ProductDetailUiState.Content(
            product = ProductDetail(
                title = "Essence Mascara Lash Princess",
                description = "The Essence Mascara Lash Princess is a popular mascara known for its volumizing and lengthening effects. Achieve dramatic lashes with this long-lasting and cruelty-free formula.",
                price = 9.99,
                currencyCode = "EUR",
                rating = 2.56,
                images = listOf("", "")
            )
        ),
        ProductDetailUiState.Error,
    )
}
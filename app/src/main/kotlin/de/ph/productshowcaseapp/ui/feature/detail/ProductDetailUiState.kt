package de.ph.productshowcaseapp.ui.feature.detail

sealed interface ProductDetailUiState {
    data object Loading : ProductDetailUiState
    data class Content(val product: ProductDetail) : ProductDetailUiState
    data object Error : ProductDetailUiState
}

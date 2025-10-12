package de.ph.productshowcaseapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ph.productshowcaseapp.domain.Product
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductDetailViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _product = MutableStateFlow<Product?>(null)
    val product: StateFlow<Product?> = _product.asStateFlow()

    fun getProduct(id: Int) {
        viewModelScope.launch {
            productRepository.getProductById(id).collect {
                _product.value = it
            }
        }
    }
}

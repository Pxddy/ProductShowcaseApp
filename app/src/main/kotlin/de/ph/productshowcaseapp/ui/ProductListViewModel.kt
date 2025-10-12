package de.ph.productshowcaseapp.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ph.productshowcaseapp.domain.Product
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductListViewModel(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    init {
        viewModelScope.launch {
            productRepository.getProducts().collect {
                _products.value = it
            }
        }
    }
}

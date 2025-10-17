package de.ph.productshowcaseapp.ui.feature.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class ProductListViewModel @Inject constructor(
    productRepository: ProductRepository,
    private val productListItemMapper: ProductListItemMapper
) : ViewModel() {

    val products: Flow<PagingData<ProductListItem>> = productRepository.getProducts()
        .map { pagingData ->
            pagingData.map { product ->
                productListItemMapper.toUi(product)
            }
        }
        .cachedIn(viewModelScope)
}

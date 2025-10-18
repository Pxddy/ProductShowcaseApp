package de.ph.productshowcaseapp.ui.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import de.ph.productshowcaseapp.domain.ProductRepository
import de.ph.productshowcaseapp.ui.navigation.Destination
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import timber.log.Timber
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val productMapper: ProductDetailMapper,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val route: Destination.ProductDetail
        get() = savedStateHandle.toRoute()

    val uiState: StateFlow<ProductDetailUiState> = flow { emit(route.productId) }
        .flatMapLatest(productRepository::getProductById)
        .map(productMapper::toProductDetail)
        .map<ProductDetail, ProductDetailUiState>(ProductDetailUiState::Content)
        .catch {
            Timber.e(it, "Failed to load product details")
            emit(ProductDetailUiState.Error)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeout = 5.seconds),
            initialValue = ProductDetailUiState.Loading,
        )
}

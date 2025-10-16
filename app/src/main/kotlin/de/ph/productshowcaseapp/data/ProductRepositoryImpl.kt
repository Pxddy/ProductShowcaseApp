package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import de.ph.productshowcaseapp.data.local.ProductDao
import de.ph.productshowcaseapp.domain.Product
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRepositoryImpl @Inject constructor(
    private val productDao: ProductDao,
    private val productRemoteMediator: ProductRemoteMediator,
    private val productMapper: ProductMapper
) : ProductRepository {

    override fun getProducts(): Flow<PagingData<Product>> {
        return Pager(
            config = PagingConfig(pageSize = 30),
            remoteMediator = productRemoteMediator,
            pagingSourceFactory = { productDao.getProducts() }
        ).flow.map {
            it.map { productEntity ->
                productMapper.toDomain(productEntity)
            }
        }
    }

    override fun getProductById(id: Int): Flow<Product> {
        // TODO: Implement data orchestration from API and DB with caching logic
        return productDao.getProductById(id).map {
            productMapper.toDomain(it)
        }
    }
}

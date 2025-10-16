package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import de.ph.productshowcaseapp.data.local.ProductDao
import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.remote.ProductApiService
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator @Inject constructor(
    private val productDao: ProductDao,
    private val productApiService: ProductApiService
): RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        loadType
        TODO("Not yet implemented")
    }
}
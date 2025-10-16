package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import de.ph.productshowcaseapp.data.local.ProductEntity

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator constructor(): RemoteMediator<Int, ProductEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        loadType
        TODO("Not yet implemented")
    }
}
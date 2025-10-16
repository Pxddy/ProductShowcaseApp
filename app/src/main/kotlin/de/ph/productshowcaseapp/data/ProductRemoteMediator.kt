package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import de.ph.productshowcaseapp.data.local.AppDatabase
import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.local.ProductRemoteKey
import de.ph.productshowcaseapp.data.remote.ProductApiService
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator @Inject constructor(
    private val appDatabase: AppDatabase,
    private val productApiService: ProductApiService,
    private val productMapper: ProductMapper
) : RemoteMediator<Int, ProductEntity>() {

    private val productDao = appDatabase.productDao()
    private val remoteKeyDao = appDatabase.remoteKeyDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        return try {
            val pageKey = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                        ?: return MediatorResult.Success(endOfPaginationReached = false)

                    val remoteKey = remoteKeyDao.remoteKeyByProductId(lastItem.id)
                    remoteKey?.nextKey ?: return MediatorResult.Success(endOfPaginationReached = true)
                }
            }

            val response = productApiService.getProducts(
                limit = state.config.pageSize,
                skip = pageKey
            )

            val products = response.products
            val endOfPaginationReached = (pageKey + products.size) >= response.total

            if (loadType == LoadType.REFRESH && products.isEmpty() && response.total > 0) {
                return MediatorResult.Error(IOException("Inconsistent data from API"))
            }

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    productDao.clearAll()
                }

                val nextKey = if (endOfPaginationReached) null else pageKey + products.size
                val keys = products.map {
                    ProductRemoteKey(productId = it.id, nextKey = nextKey)
                }

                remoteKeyDao.upsertAll(keys)
                productDao.upsertAll(productMapper.toEntityList(products))
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }
}

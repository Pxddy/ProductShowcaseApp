package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import de.ph.productshowcaseapp.data.local.AppDatabase
import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.remote.ProductApiService
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediator @Inject constructor(
    private val appDatabase: AppDatabase,
    private val productApiService: ProductApiService,
    private val productMapper: ProductMapper
) : RemoteMediator<Int, ProductEntity>() {

    private val productDao = appDatabase.productDao()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ProductEntity>
    ): MediatorResult {
        Timber.d("==> load(loadType=$loadType, state=$state)")

        return try {
            val skip = when (loadType) {
                LoadType.REFRESH -> 0
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                    .also { Timber.d("PREPEND requested, returning $it") }

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    Timber.d("APPEND -> Current item id: ${lastItem?.id}")
                    lastItem?.id ?: return MediatorResult.Success(endOfPaginationReached = false)
                }
            }

            Timber.d("Requesting API: getProducts(limit=${state.config.pageSize}, skip=$skip)")
            val response = productApiService.getProducts(
                limit = state.config.pageSize,
                skip = skip,
            )

            val products = response.products
            val endOfPaginationReached = (skip + products.size) >= response.total
            Timber.d("API Response: products.size=${products.size}, total=${response.total}")

            if (loadType == LoadType.REFRESH && products.isEmpty() && response.total > 0) {
                val error = IOException(
                    "Refresh returned empty list, but total count is ${response.total}. Possible data inconsistency."
                )
                Timber.w(error, "Empty response on refresh.")
                return MediatorResult.Error(error)
            }

            appDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    Timber.d("REFRESH -> Clearing all products.")
                    productDao.clearAll()
                }

                val entityList = productMapper.toEntityList(products)
                productDao.upsertAll(products = entityList)
                Timber.d("Upserted ${entityList.size} products into database.")
            }

            Timber.d("<== load() finished successfully. endOfPaginationReached=$endOfPaginationReached")
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            Timber.e(e, "Failed to load remote data for loadType: $loadType")
            when (e) {
                is IOException, is HttpException -> MediatorResult.Error(e)
                else -> throw e
            }
            MediatorResult.Error(e)
        }
    }
}

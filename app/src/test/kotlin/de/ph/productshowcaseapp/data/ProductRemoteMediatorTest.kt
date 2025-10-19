package de.ph.productshowcaseapp.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.RoomDatabase
import androidx.room.withTransaction
import de.ph.productshowcaseapp.data.local.AppDatabase
import de.ph.productshowcaseapp.data.local.ProductDao
import de.ph.productshowcaseapp.data.local.ProductEntity
import de.ph.productshowcaseapp.data.remote.ProductApiService
import de.ph.productshowcaseapp.data.remote.ProductDto
import de.ph.productshowcaseapp.data.remote.ProductListResponse
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.HttpException
import java.io.IOException

/**
 * Unit tests for the [ProductRemoteMediator].
 */
@OptIn(ExperimentalPagingApi::class)
class ProductRemoteMediatorTest {

    // Mocks for all dependencies
    private lateinit var appDatabase: AppDatabase
    private lateinit var productApiService: ProductApiService
    private lateinit var productMapper: ProductMapper
    private lateinit var productDao: ProductDao

    // System Under Test
    private lateinit var sut: ProductRemoteMediator

    @BeforeEach
    fun setUp() {
        // 1. Initialize mocks

        appDatabase = mockk()
        productApiService = mockk()
        productMapper = mockk(relaxed = true)
        productDao = mockk(relaxed = true)

        // 2. Stub DAO providers
        every { appDatabase.productDao() } returns productDao

        // 3. mock withTransaction
        mockkStatic("androidx.room.RoomDatabaseKt")
        coEvery { any<RoomDatabase>().withTransaction<Any>(block = any()) } coAnswers {
            val lambda = arg<suspend () -> Any>(1)
            lambda.invoke()
        }

        // 3. Instantiate the System Under Test
        sut = ProductRemoteMediator(appDatabase, productApiService, productMapper)
    }

    @AfterEach
    fun teardown() {
        unmockkStatic("androidx.room.RoomDatabaseKt")
    }

    @Test
    fun `load WHEN refresh is called AND api succeeds THEN returns success and clears old data`() = runTest {
        // Given: A successful API response for the first page.
        val apiResponse = ProductListResponse(products = listOf(mockk<ProductDto>()), total = 20, skip = 0, limit = 10)
        coEvery {
            productApiService.getProducts(
                limit = any(),
                skip = 0,
                sortBy = any(),
                order = any()
            )
        } returns apiResponse
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.REFRESH.
        val result = sut.load(LoadType.REFRESH, pagingState)

        // Then: The result should be MediatorResult.Success.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe false

        // Then: The database `clearAll` method should be called once.
        coVerify(exactly = 1) { productDao.clearAll() }

        // Then: The new data should be upserted into the database.
        coVerify(exactly = 1) { productDao.upsertAll(any()) }
    }

    @Test
    fun `load WHEN refresh is called AND api returns io exception THEN returns error result`() = runTest {
        // Given: The API service throws an IOException.
        val ioException = IOException("Network error")
        coEvery { productApiService.getProducts(any(), any(), any(), any()) } throws ioException
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.REFRESH.
        val result = sut.load(LoadType.REFRESH, pagingState)

        // Then: The result should be MediatorResult.Error.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
        (result as RemoteMediator.MediatorResult.Error).throwable shouldBe ioException
    }

    @Test
    fun `load WHEN refresh is called AND api returns http exception THEN returns error result`() = runTest {
        // Given: The API service throws an HttpException.
        val httpException = mockk<HttpException> {
            every { message } returns "Not Found"
        }
        coEvery { productApiService.getProducts(any(), any(), any(), any()) } throws httpException
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.REFRESH.
        val result = sut.load(LoadType.REFRESH, pagingState)

        // Then: The result should be MediatorResult.Error.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
        (result as RemoteMediator.MediatorResult.Error).throwable shouldBe httpException
    }

    @Test
    fun `load WHEN refresh returns empty list with more items available THEN returns error result`() = runTest {
        // Given: A successful but empty API response, while the total count indicates more items are available.
        val apiResponse = ProductListResponse(products = emptyList(), total = 20, skip = 0, limit = 10)
        coEvery { productApiService.getProducts(any(), any(), any(), any()) } returns apiResponse
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.REFRESH.
        val result = sut.load(LoadType.REFRESH, pagingState)

        // Then: The result should be MediatorResult.Error to indicate data inconsistency.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Error>()
        (result as RemoteMediator.MediatorResult.Error).throwable.shouldBeInstanceOf<IOException>()
    }

    @Test
    fun `load WHEN append is called AND api succeeds THEN returns success and appends new data`() = runTest {
        // Given: A successful API response for the next page.
        val apiResponse = ProductListResponse(products = listOf(mockk<ProductDto>()), total = 30, skip = 10, limit = 10)
        coEvery {
            productApiService.getProducts(
                limit = any(),
                skip = 10,
                sortBy = any(),
                order = any()
            )
        } returns apiResponse

        val lastItem = mockk<ProductEntity> { every { id } returns 10 }
        val pages = listOf(
            PagingSource.LoadResult.Page<Int, ProductEntity>(
                data = listOf(lastItem),
                prevKey = null,
                nextKey = null
            )
        )
        val pagingState = PagingState(pages, null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.APPEND.
        val result = sut.load(LoadType.APPEND, pagingState)

        // Then: The result should be MediatorResult.Success.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe false

        // Then: The database `clearAll` method should NOT be called.
        coVerify(exactly = 0) { productDao.clearAll() }

        // Then: The new data should be upserted into the database.
        coVerify(exactly = 1) { productDao.upsertAll(any()) }
    }

    @Test
    fun `load WHEN append is called AND api returns empty list THEN returns success with endOfPaginationReached`() =
        runTest {
            // Given: A successful but empty API response for the next page.
            val apiResponse = ProductListResponse(products = emptyList(), total = 20, skip = 20, limit = 10)
            coEvery {
                productApiService.getProducts(
                    limit = any(),
                    skip = 20,
                    sortBy = any(),
                    order = any()
                )
            } returns apiResponse

            val lastItem = mockk<ProductEntity> { every { id } returns 20 }
            val pages = listOf(
                PagingSource.LoadResult.Page<Int, ProductEntity>(
                    data = listOf(lastItem),
                    prevKey = null,
                    nextKey = null
                )
            )
            val pagingState = PagingState(pages, null, PagingConfig(10), 0)

            // When: The load function is called with LoadType.APPEND.
            val result = sut.load(LoadType.APPEND, pagingState)

            // Then: The result should be MediatorResult.Success with endOfPaginationReached = true.
            result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
            (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe true
        }

    @Test
    fun `load WHEN append is called AND last item is null THEN returns success`() = runTest {
        // Given: The PagingState's last item is null (e.g., initial empty state).
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.APPEND.
        val result = sut.load(LoadType.APPEND, pagingState)

        // Then: The result should be MediatorResult.Success, but pagination is not considered ended.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe false
    }

    @Test
    fun `load WHEN prepend is called THEN returns success with endOfPaginationReached`() = runTest {
        // Given: Any state.
        val pagingState = PagingState<Int, ProductEntity>(listOf(), null, PagingConfig(10), 0)

        // When: The load function is called with LoadType.PREPEND.
        val result = sut.load(LoadType.PREPEND, pagingState)

        // Then: The result should be MediatorResult.Success with endOfPaginationReached = true.
        result.shouldBeInstanceOf<RemoteMediator.MediatorResult.Success>()
        (result as RemoteMediator.MediatorResult.Success).endOfPaginationReached shouldBe true
    }
}

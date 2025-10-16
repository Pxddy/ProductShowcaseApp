package de.ph.productshowcaseapp.di

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import de.ph.productshowcaseapp.data.ProductRepositoryImpl
import de.ph.productshowcaseapp.data.local.AppDatabase
import de.ph.productshowcaseapp.data.local.ProductDao
import de.ph.productshowcaseapp.data.remote.ProductApiService
import de.ph.productshowcaseapp.domain.ProductRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindProductRepository(impl: ProductRepositoryImpl): ProductRepository

    companion object {

        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
            return Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                "product_showcase.db"
            ).build()
        }

        @Provides
        fun provideProductDao(db: AppDatabase): ProductDao {
            return db.productDao()
        }

        @Provides
        @Singleton
        fun provideProductApiService(): ProductApiService {
            // TODO: Implement with Retrofit
            return object : ProductApiService {
                override suspend fun getProducts(limit: Int, skip: Int) = TODO("Provide a real implementation")
            }
        }
    }
}

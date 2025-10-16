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
import de.ph.productshowcaseapp.data.local.ProductRemoteKeyDao
import de.ph.productshowcaseapp.data.remote.ProductApiService
import de.ph.productshowcaseapp.domain.ProductRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://dummyjson.com/"

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
        fun provideRemoteKeyDao(db: AppDatabase): ProductRemoteKeyDao {
            return db.remoteKeyDao()
        }

        @Provides
        @Singleton
        fun provideJson(): Json {
            return Json {
                ignoreUnknownKeys = true
            }
        }

        @Provides
        @Singleton
        fun provideOkHttpClient(): OkHttpClient {
            return OkHttpClient.Builder().build()
        }

        @Provides
        @Singleton
        fun provideRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .build()
        }

        @Provides
        @Singleton
        fun provideProductApiService(retrofit: Retrofit): ProductApiService {
            return retrofit.create(ProductApiService::class.java)
        }
    }
}

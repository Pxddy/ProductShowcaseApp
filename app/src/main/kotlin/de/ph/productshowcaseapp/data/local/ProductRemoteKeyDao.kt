package de.ph.productshowcaseapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ProductRemoteKeyDao {

    @Upsert
    suspend fun upsertAll(remoteKeys: List<ProductRemoteKey>)

    @Query("SELECT * FROM product_remote_keys WHERE product_id = :productId")
    suspend fun remoteKeyByProductId(productId: Int): ProductRemoteKey?
}

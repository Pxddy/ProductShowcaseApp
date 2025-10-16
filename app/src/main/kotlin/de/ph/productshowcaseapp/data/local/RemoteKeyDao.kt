package de.ph.productshowcaseapp.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface RemoteKeyDao {

    @Upsert
    suspend fun upsertAll(remoteKeys: List<RemoteKey>)

    @Query("SELECT * FROM remote_keys WHERE product_id = :productId")
    suspend fun remoteKeyByProductId(productId: Int): RemoteKey?

    @Query("DELETE FROM remote_keys")
    suspend fun clearRemoteKeys()
}

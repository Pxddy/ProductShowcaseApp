package de.ph.productshowcaseapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class ProductRemoteKey(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)

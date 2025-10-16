package de.ph.productshowcaseapp.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "product_remote_keys",
    foreignKeys = [
        ForeignKey(
            entity = ProductEntity::class,
            parentColumns = ["id"],
            childColumns = ["product_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class ProductRemoteKey(
    @PrimaryKey
    @ColumnInfo(name = "product_id")
    val productId: Int,

    @ColumnInfo(name = "next_key")
    val nextKey: Int?
)

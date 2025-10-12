package de.ph.productshowcaseapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val description: String,
    val price: Double,
    val rating: Double,
    val category: String,
    val thumbnail: String,
    val images: String // Store as JSON string or use a TypeConverter for List<String>
)

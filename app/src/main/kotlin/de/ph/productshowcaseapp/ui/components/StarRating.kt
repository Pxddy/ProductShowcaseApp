package de.ph.productshowcaseapp.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlin.math.floor

@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    maxRating: Int = 5
) {
    Row(modifier = modifier) {
        val fullStars = floor(rating).toInt()
        val halfStars = if (rating - fullStars >= 0.5) 1 else 0
        val emptyStars = maxRating - fullStars - halfStars

        repeat(fullStars) {
            Icon(imageVector = Icons.Filled.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
        repeat(halfStars) {
            Icon(imageVector = Icons.AutoMirrored.Filled.StarHalf, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
        repeat(emptyStars) {
            Icon(imageVector = Icons.Filled.StarBorder, contentDescription = null, tint = MaterialTheme.colorScheme.secondary)
        }
    }
}

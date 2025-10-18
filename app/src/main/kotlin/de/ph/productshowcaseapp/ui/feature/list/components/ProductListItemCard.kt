package de.ph.productshowcaseapp.ui.feature.list.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import de.ph.productshowcaseapp.R
import de.ph.productshowcaseapp.ui.feature.list.ProductListItem
import de.ph.productshowcaseapp.ui.theme.ProductShowcaseAppTheme
import de.ph.productshowcaseapp.ui.theme.spacing
import de.ph.productshowcaseapp.ui.util.rememberFormattedPrice

@Composable
fun ProductListItemCard(
    product: ProductListItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val formattedPrice = rememberFormattedPrice(
        price = product.price,
        currencyCode = product.currencyCode,
    )

    ListItem(
        modifier = modifier
            .clickable(
                onClick = onClick,
                onClickLabel = stringResource(R.string.product_list_item_click_label, product.title),
            ),
        leadingContent = {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = null,
                modifier = Modifier
                    .size(96.dp)
                    .clip(MaterialTheme.shapes.large),
                contentScale = ContentScale.Crop,
            )
        },
        headlineContent = {
            Text(
                text = product.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
            )
        },
        overlineContent = {
            Text(
                text = product.category,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        supportingContent = {
            Text(
                text = formattedPrice,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = MaterialTheme.spacing.xs),
            )
        },
        colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.background),
    )
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductListItemCardPreview() {
    ProductShowcaseAppTheme {
        ProductListItemCard(
            product = ProductListItem(
                id = 0,
                thumbnail = "",
                title = "Essence Mascara Lash Princess",
                price = 9.99,
                currencyCode = "EUR",
                category = "Beauty"
            ),
            onClick = {},
        )
    }
}
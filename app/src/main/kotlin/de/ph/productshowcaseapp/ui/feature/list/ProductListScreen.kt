package de.ph.productshowcaseapp.ui.feature.list

import android.content.res.Configuration
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import de.ph.productshowcaseapp.R
import de.ph.productshowcaseapp.ui.feature.list.components.ProductListItemCard
import de.ph.productshowcaseapp.ui.theme.ProductShowcaseAppTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun ProductListScreen(
    viewModel: ProductListViewModel = hiltViewModel()
) {
    val products = viewModel.products.collectAsLazyPagingItems()
    ProductListScreenContent(products = products)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductListScreenContent(
    products: LazyPagingItems<ProductListItem>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.product_list_title),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
            )
        },
        contentWindowInsets = WindowInsets.safeDrawing,
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = paddingValues,
        ) {
            items(
                count = products.itemCount,
                key = products.itemKey { it },
                contentType = products.itemContentType { it.type },
            ) { index ->
                products[index]?.let { product ->
                    ProductListItemCard(product = product, onClick = {})
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductListScreenPreview() {
    ProductShowcaseAppTheme {
        val dummyProducts = flowOf(
            PagingData.from(
                listOf(
                    ProductListItem(
                        id = 1,
                        thumbnail = "",
                        title = "Essence Mascara Lash Princess",
                        price = 9.99,
                        currencyCode = "EUR",
                        category = "Beauty"
                    ),
                    ProductListItem(
                        id = 2,
                        thumbnail = "",
                        title = "Eyeshadow Palette with Mirror",
                        price = 19.99,
                        currencyCode = "EUR",
                        category = "Beauty"
                    )
                )
            )
        ).collectAsLazyPagingItems()

        ProductListScreenContent(products = dummyProducts)
    }
}

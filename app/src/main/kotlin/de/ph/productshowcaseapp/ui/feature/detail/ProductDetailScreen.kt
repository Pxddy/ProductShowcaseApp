package de.ph.productshowcaseapp.ui.feature.detail

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import de.ph.productshowcaseapp.R
import de.ph.productshowcaseapp.ui.components.StarRating
import de.ph.productshowcaseapp.ui.theme.ProductShowcaseAppTheme
import de.ph.productshowcaseapp.ui.theme.spacing
import de.ph.productshowcaseapp.ui.util.rememberFormattedNumber
import de.ph.productshowcaseapp.ui.util.rememberFormattedPrice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun ProductDetailScreen(
    onBackClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductDetailScreenContent(
        uiState = uiState,
        onBackClick = onBackClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProductDetailScreenContent(
    uiState: ProductDetailUiState,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (uiState is ProductDetailUiState.Content) {
                        Text(
                            text = uiState.product.title,
                            style = MaterialTheme.typography.titleSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.navigation_back_icon_content_description),
                            )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (uiState) {
                is ProductDetailUiState.Content -> ProductDetailContent(product = uiState.product)
                ProductDetailUiState.Loading -> CircularProgressIndicator()
                ProductDetailUiState.Error -> Text(
                    text = stringResource(R.string.product_detail_error_message),
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductDetailContent(product: ProductDetail) {
    val pagerState = rememberPagerState { product.images.size }
    val scope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = MaterialTheme.spacing.m)
    ) {
        item {
            ProductImagePager(
                pagerState = pagerState,
                scope = scope,
                images = product.images,
                modifier = Modifier
                    .fillMaxWidth()
                    .clearAndSetSemantics {},
            )
        }
        item {
            Column(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.s)) {
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.m))
                ProductInfo(
                    title = product.title,
                    price = product.price,
                    currencyCode = product.currencyCode,
                    rating = product.rating
                )
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = MaterialTheme.spacing.m),
                    color = MaterialTheme.colorScheme.outline,
                )
                ProductDescription(
                    description = product.description,
                    modifier = Modifier.semantics(mergeDescendants = true) {},
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ProductImagePager(
    pagerState: PagerState,
    scope: CoroutineScope,
    images: List<String>,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxWidth(),
        ) { page ->
            AsyncImage(
                model = images.getOrNull(page),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.xs),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AnimatedVisibility(visible = pagerState.targetPage > 0) {
                CarouselButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    onClick = {
                        scope.launch {
                            val prevPage = (pagerState.currentPage - 1)
                            pagerState.animateScrollToPage(prevPage)
                        }
                    },
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            AnimatedVisibility(visible = pagerState.targetPage < pagerState.pageCount - 1) {
                CarouselButton(
                    icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    onClick = {
                        scope.launch {
                            val nextPage = (pagerState.currentPage + 1)
                            pagerState.animateScrollToPage(nextPage)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun CarouselButton(
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    IconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
        shape = CircleShape,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
        )
    }
}

@Composable
private fun ProductInfo(title: String, price: Double, currencyCode: String, rating: Double) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineLarge,
        color = MaterialTheme.colorScheme.onBackground
    )
    Spacer(modifier = Modifier.height(MaterialTheme.spacing.s))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = rememberFormattedPrice(price = price, currencyCode = currencyCode),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary
        )

        val maxRating = 5
        val formattedRating = rememberFormattedNumber(number = rating, maximumFractionDigits = 2)
        val ratingDescription = stringResource(
            R.string.product_detail_star_rating_content_description,
            formattedRating, maxRating)
        Row(
            modifier = Modifier.clearAndSetSemantics { contentDescription = ratingDescription },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs)
        ) {
            StarRating(rating = rating)
            Text(
                text = "($formattedRating)",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ProductDescription(
    description: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.xs),
    ) {
        Text(
            text = stringResource(R.string.product_detail_description_title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Preview(name = "Light Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Preview(name = "Dark Mode", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProductDetailScreenPreview(
    @PreviewParameter(ProductDetailScreenPreviewParameterProvider::class)
    state: ProductDetailUiState
) {
    ProductShowcaseAppTheme {
        ProductDetailScreenContent(
            uiState = state,
            onBackClick = {},
        )
    }
}


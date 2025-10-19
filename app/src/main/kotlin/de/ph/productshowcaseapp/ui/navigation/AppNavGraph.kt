package de.ph.productshowcaseapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import de.ph.productshowcaseapp.ui.feature.detail.ProductDetailScreen
import de.ph.productshowcaseapp.ui.feature.list.ProductListScreen

@Composable
fun AppNavGraph(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destination.ProductList,
        modifier = modifier
    ) {
        composable<Destination.ProductList> {
            ProductListScreen(onProductClick = navController::navigateToDetails)
        }

        composable<Destination.ProductDetail> {
            ProductDetailScreen(onBackClick = navController::navigateUp)
        }
    }
}

private fun NavController.navigateToDetails(productId: Int) {
    navigate(Destination.ProductDetail(productId = productId))
}
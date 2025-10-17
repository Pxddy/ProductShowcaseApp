package de.ph.productshowcaseapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacing(
    val xs: Dp = 8.dp,
    val s: Dp = 16.dp,
    val m: Dp = 24.dp,
    val l: Dp = 32.dp,
)

val LocalSpacing = staticCompositionLocalOf<Spacing> {
    error("CompositionLocal LocalSpacing not provided")
}

val MaterialTheme.spacing: Spacing
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacing.current

package de.ph.productshowcaseapp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import java.text.NumberFormat

@Composable
fun rememberFormattedNumber(
    number: Double,
    maximumFractionDigits: Int,
    locale: Locale = Locale.current,
): String {

    val formatter = remember(maximumFractionDigits) {
        NumberFormat.getInstance(locale.platformLocale)
            .apply { this.maximumFractionDigits = maximumFractionDigits }
    }

    return formatter.format(number)
}
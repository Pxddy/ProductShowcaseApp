package de.ph.productshowcaseapp.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.intl.Locale
import java.text.NumberFormat
import java.util.Currency


/**
 * A Composable helper function that remembers a formatted currency string.
 * It correctly handles locale changes and ensures the price is formatted
 * according to the user's device settings.
 *
 * @param price The price to be formatted.
 * @param currencyCode The ISO 4217 currency code (e.g., "EUR", "USD").
 * @return A locale-specific formatted currency string (e.g., "9,99 €", "$9.99").
 */
@Composable
fun rememberFormattedPrice(price: Double, currencyCode: String): String {
    val currentLocale = Locale.current
    val numberFormat = remember(currentLocale, currencyCode) {
        NumberFormat.getCurrencyInstance(currentLocale.platformLocale).apply {
            runCatching { Currency.getInstance(currencyCode) }
                .onSuccess { validCurrency ->
                    this.currency = validCurrency
                }
        }
    }

    return numberFormat.format(price)
}
package com.ryancasler.solscan.core

import java.text.NumberFormat
import java.util.*

fun Double.toCurrencyString(): String {
    val formatter: NumberFormat = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(this)
}

fun Int.centsToCurrencyString(): String = (this / 100.0).toCurrencyString()
fun Long.centsToCurrencyString(): String = (this / 100.0).toCurrencyString()
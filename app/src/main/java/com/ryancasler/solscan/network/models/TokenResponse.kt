package com.ryancasler.solscan.network.models

import com.ryancasler.solscan.core.Lamports

data class Token (
    val tokenAddress: String,
    val tokenAmount: TokenAmount,
    val tokenAccount: String,
    val tokenName: String,
    val tokenIcon: String,
    val rentEpoch: Long,
    val lamports: Lamports,
    val tokenSymbol: String? = null,
    val priceUsdt: Double? = null
)

data class TokenAmount (
    val amount: String,
    val decimals: Long,
    val uiAmount: Double,
    val uiAmountString: String
)

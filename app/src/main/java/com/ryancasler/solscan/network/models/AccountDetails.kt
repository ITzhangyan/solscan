package com.ryancasler.solscan.network.models

import com.ryancasler.solscan.core.Lamports
import com.ryancasler.solscan.core.toSol

data class AccountDetails(
    val lamports: Lamports,
    val type: String,
    val rentEpoch: Int,
    val account: String
) {
    val solAmount: Double
        get() = lamports.toSol()
}
package com.ryancasler.solscan.screens.addwallet

import androidx.lifecycle.ViewModel
import com.ryancasler.solscan.core.compose.EditTextState

class AddWalletViewModel: ViewModel() {
    val editTextState = EditTextState().apply {
        text.value = "HcnPrRgDfzvy65eqYYjjgCsS19WjTM5CspWPC9oKvwd1"
    }
}
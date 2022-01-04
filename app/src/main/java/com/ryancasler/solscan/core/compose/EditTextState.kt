package com.ryancasler.solscan.core.compose

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class EditTextState {
    fun get() = text.value.trim()

    var text: MutableState<String> = mutableStateOf("")
    val error: MutableState<Boolean> = mutableStateOf(false)
}
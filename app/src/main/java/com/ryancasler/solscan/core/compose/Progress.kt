package com.ryancasler.solscan.core.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.material.progressindicator.CircularProgressIndicator

@Composable
fun FullScreenLoadingSpinner(
    text: String = ""
) {
    LoadingSpinner(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        text
    )
}

@Composable
fun LoadingSpinner(
    modifier: Modifier = Modifier,
    text: String = "",
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        CircularProgressIndicator()
        Text(text, modifier = Modifier.padding(top = 16.dp))
    }
}
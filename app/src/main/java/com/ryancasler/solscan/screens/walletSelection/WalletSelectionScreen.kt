package com.ryancasler.solscan

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable


@Composable
fun WalletSelection(

) {
    Column {
        Text("Select your wallet below or add a new one")

        Button(onClick = { /*
        TODO navigate to the
        */ }) {
            Text(text = "Add Wallet")
        }
    }
}
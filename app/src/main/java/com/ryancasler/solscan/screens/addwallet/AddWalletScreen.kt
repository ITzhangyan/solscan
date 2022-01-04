package com.ryancasler.solscan.screens.addwallet

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ryancasler.solscan.screens.SolScanDestinations.buildWalletDetailPath

@Composable
@Preview
fun AddWalletScreen(
    viewModel: AddWalletViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Enter Your Wallet Address")

        val state = viewModel.editTextState

        OutlinedTextField(
            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
            label = { Text("Address") },
            isError = state.error.value,
            value = state.text.value,
            onValueChange = { state.text.value = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )

        Button(modifier = Modifier.padding(top = 16.dp), onClick = {
            navController.navigate(buildWalletDetailPath(state.text.value)) {
                launchSingleTop = true
                restoreState = true
            }
        }) {
            Text(text = "View Wallet")
        }
    }
}
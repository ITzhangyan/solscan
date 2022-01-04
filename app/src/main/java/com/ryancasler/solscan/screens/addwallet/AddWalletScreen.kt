package com.ryancasler.solscan.screens.addwallet

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ryancasler.solscan.R
import com.ryancasler.solscan.screens.SolScanDestinations.buildWalletDetailPath
import com.ryancasler.solscan.ui.theme.Purple700
import com.ryancasler.solscan.ui.theme.Teal200

@Composable
@Preview
fun AddWalletScreen(
    viewModel: AddWalletViewModel = hiltViewModel(),
    navController: NavController = rememberNavController()
) {
    Box(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(bottom = 16.dp),
                color = Purple700,
                style = MaterialTheme.typography.h4,
                text = "SolViewer"
            )

            Text(text = "Enter Your Public Wallet Address")

            val state = viewModel.editTextState

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
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

        Column(
            modifier = Modifier.align(alignment = Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 8.dp
                ),
                style = MaterialTheme.typography.subtitle2,
                text = "Powered by"
            )
            Image(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp,
                    top = 8.dp
                ),
                painter = painterResource(id = R.drawable.solscan_logo),
                contentDescription = "solscan"
            )
        }
    }
}
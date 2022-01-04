package com.ryancasler.solscan.screens.nftdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ryancasler.solscan.core.compose.Animated
import com.ryancasler.solscan.core.compose.FullScreenLoadingSpinner

@Composable
fun NftDetailScreen(
    viewModel: NftDetailViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val state = viewModel.state.value

    Animated(visible = state is NftDetailViewModel.DetailState.Loading) {
        FullScreenLoadingSpinner("Loading details...")
    }

    Animated(visible = state is NftDetailViewModel.DetailState.Loaded) {
        NFTDetail((state as NftDetailViewModel.DetailState.Loaded).nft)
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NFTDetail(
    nft: NftDetailViewModel.NFT
) {
    Text(text = nft.description)

    LazyVerticalGrid(
        cells = GridCells.Fixed(3)
    ) {
        items(nft.attributes) { attribute ->
            Card(
                modifier = Modifier.fillMaxSize(),
                border = BorderStroke(2.dp, Color.Red),
            ) {
                Text(text = attribute.traitType)
                Text(text = attribute.value)
            }
        }
    }
}
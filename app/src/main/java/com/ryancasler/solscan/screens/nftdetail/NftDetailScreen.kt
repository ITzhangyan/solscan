package com.ryancasler.solscan.screens.nftdetail

import android.os.Build
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.transform.CircleCropTransformation
import coil.transform.RoundedCornersTransformation
import coil.util.CoilUtils
import com.google.accompanist.flowlayout.FlowRow
import com.ryancasler.solscan.R
import com.ryancasler.solscan.core.compose.Animated
import com.ryancasler.solscan.core.compose.FullScreenLoadingSpinner
import com.ryancasler.solscan.core.theme.Purple700
import com.ryancasler.solscan.core.theme.Teal200
import com.ryancasler.solscan.network.models.Attribute
import com.ryancasler.solscan.network.models.addDebugLogging
import okhttp3.OkHttpClient

@Composable
fun NftDetailScreen(
    viewModel: NftDetailViewModel = viewModel()
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
    val painter = if (nft.image.isBlank()) {
        painterResource(id = R.drawable.ic_baseline_broken_image_24)
    } else {
        rememberImagePainter(nft.image)
    }

    Column(
        Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .aspectRatio(1f)
                .fillMaxWidth(),
            painter = painter,
            contentDescription = null
        )

        LazyColumn {
            item {
                Text(
                    modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp),
                    text = nft.name,
                    style = MaterialTheme.typography.h6,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = nft.description,
                    modifier = Modifier.padding(
                        top = 8.dp,
                        bottom = 16.dp,
                        start = 16.dp,
                        end = 16.dp
                    )
                )
            }

            item {
                FlowRow(
                    modifier = Modifier.padding(8.dp)
                ) {
                    nft.attributes.forEach {
                        AttributeItem(it)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun AttributeItem(
    attribute: Attribute = Attribute("Test Trait", "Test val")
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (screenWidth / 2) - 24.dp

    Card(
        modifier = Modifier
            .width(cardWidth)
            .padding(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(text = attribute.traitType.toUpperCase())

            Text(
                text = attribute.value,
                color = Purple700,
                style = MaterialTheme.typography.h6
            )
        }
    }
}
package com.ryancasler.solscan.screens.walletdetails

import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyScopeMarker
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterEnd
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Alignment.Companion.TopEnd
import androidx.compose.ui.Alignment.Companion.TopStart
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.imageLoader
import coil.transform.CircleCropTransformation
import coil.util.CoilUtils
import com.google.accompanist.flowlayout.FlowRow
import com.google.accompanist.placeholder.PlaceholderHighlight
import com.google.accompanist.placeholder.material.placeholder
import com.google.accompanist.placeholder.material.shimmer
import com.ryancasler.solscan.network.models.addDebugLogging
import okhttp3.OkHttpClient
import com.ryancasler.solscan.R
import com.ryancasler.solscan.core.compose.Animated
import com.ryancasler.solscan.core.compose.FullScreenLoadingSpinner
import com.ryancasler.solscan.core.compose.LoadingSpinner
import com.ryancasler.solscan.core.theme.Typography
import com.ryancasler.solscan.core.toCurrencyString
import com.ryancasler.solscan.network.models.MarketDetails

@Composable
@Preview
fun WalletScreen(
    walletDetailViewModel: WalletDetailViewModel = viewModel(),
    navController: NavController = rememberNavController()
) {
    val state = walletDetailViewModel.state.value

    Animated(visible = state is WalletDetailState.Loading) {
        FullScreenLoadingSpinner("Loading Wallet...")
    }

    Animated(visible = state is WalletDetailState.Loaded) {
        WalletLoaded(state as WalletDetailState.Loaded, navController)
    }

    Animated(visible = state is WalletDetailState.Error) {
        ErrorLoadingWallet()
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Preview
fun WalletLoaded(
    walletLoaded: WalletDetailState.Loaded = WalletDetailState.Loaded(
        "rrc.sol",
        "rrc.sol",
        "$0.00",
        emptyList(),
        MarketDetails("", 1.1, -100.0),
        NftState.NoNfts
    ), navController: NavController = rememberNavController()
) {
    Column {

        Box(
            Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                Modifier.align(TopStart)
            ) {
                Text(
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.h5,
                    text = walletLoaded.shortAddress
                )

                Text(
                    style = Typography.subtitle1,
                    textAlign = TextAlign.Start,
                    text = walletLoaded.totalValueUSD
                )
            }

            Column(
                Modifier.align(TopEnd)
            ) {
                val solanaDetails = walletLoaded.solanaDetails
                Text(
                    style = MaterialTheme.typography.h5,
                    textAlign = TextAlign.End,
                    text = "Solana"
                )

                Text(
                    textAlign = TextAlign.End,
                    text = solanaDetails.current_price.toCurrencyString()
                )

                Text(
                    style = Typography.subtitle1,
                    textAlign = TextAlign.End,
                    text = "24hrs: ${solanaDetails.price_change_percentage_24h}%"
                )
            }
        }

        LazyColumn(
            Modifier.padding(start = 8.dp, end = 8.dp)
        ) {

            items(walletLoaded.tokens) { token ->
                FungibleTokenItem(token)
                Divider(color = Color.LightGray)
            }

            item {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                )

                when (walletLoaded.nfts) {
                    is NftState.Loaded -> FlowRow {
                        walletLoaded.nfts.nfts.forEach {
                            Card(
                                onClick = {
                                    // TODO nav graph not being cool
//                                navController.navigate(buildNftDetailPath(it.address)) {
//                                    launchSingleTop = true
//                                    restoreState = true
//                                }
                                },
                                elevation = 0.dp,
                                border = BorderStroke(0.dp, Color.Transparent),
                                backgroundColor = Color.Transparent
                            ) {
                                NonFungibleToken(it)
                            }
                        }
                    }

                    NftState.Loading -> LoadingSpinner(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(), text = "Loading NFTs"
                    )
                    NftState.NoNfts -> Text(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        text = "No NFTs"
                    )
                }
            }
        }
    }
}

@LazyScopeMarker
fun LazyListScope.header(
    text: String = ""
) {
    item {
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
        ) {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 8.dp)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = text
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun NonFungibleToken(
    token: NFT = NFT("Ramp Coin", "", "")
) {
    val imageLoader = LocalContext.current.imageLoader

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val cardWidth = (screenWidth / 2) - 24.dp
    val showLoading = remember { mutableStateOf(true) }

    Column(
        Modifier
            .padding(8.dp)
            .width(cardWidth)
    ) {
        Image(
            modifier = Modifier
                .size(cardWidth)
                .placeholder(
                    visible = showLoading.value,
                    highlight = PlaceholderHighlight.shimmer()
                ),
            painter = rememberImagePainter(
                data = token.previewUrl,
                imageLoader = imageLoader,
                builder = {
                    listener(onSuccess = { _, _ -> showLoading.value = false })
                }
            ),
            contentDescription = token.name,
        )

        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = token.name,
            style = Typography.subtitle2
        )
    }
}

@Composable
@Preview
fun ErrorLoadingWallet() {
    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            text = "There was an error loading your wallet.\nlease try again.",
            style = Typography.h5,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
@Preview
fun EmptyWallet() {
    Box(Modifier.fillMaxSize()) {
        Text(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(32.dp),
            text = "This Wallet appears empty." +
                    "\nThis is a public, view only app." +
                    "\nPlease view in your wallet to verify.",
            style = Typography.h5,
            textAlign = TextAlign.Center
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun FungibleTokenItem(
    token: FungibleToken = FungibleToken("Ramp Coin", "Ramp", 6.9, "$4.20", "", "")
) {
    Row(
        Modifier
            .padding(8.dp)
    ) {
        val context = LocalContext.current
        val imageLoader = ImageLoader.Builder(context)
            .crossfade(true)
            .componentRegistry {
                add(SvgDecoder(context))
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder(context))
                } else {
                    add(GifDecoder())
                }
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .addDebugLogging()
                    .cache(CoilUtils.createDefaultCache(context))
                    .build()
            }
            .build()

        val painter = if (token.imageUrl.isBlank()) {
            painterResource(id = R.drawable.ic_baseline_broken_image_24)
        } else {
            rememberImagePainter(
                imageLoader = imageLoader,
                data = token.imageUrl,
                builder = {
                    transformations(CircleCropTransformation())
                }
            )
        }

        Image(
            modifier = Modifier
                .padding(end = 16.dp)
                .align(Alignment.CenterVertically)
                .weight(1f)
                .size(48.dp),
            painter = painter,
            contentDescription = null,
        )

        Column(
            Modifier
                .fillMaxHeight()
                .weight(3f)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 16.dp)
                    .align(Start),
                text = token.name,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

            Text(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .align(Start),
                text = "${token.quantity} ${token.symbol}",
                fontSize = 12.sp
            )
        }

        Column(
            Modifier
                .fillMaxHeight()
                .padding(start = 16.dp)
                .weight(3f)
        ) {
            Text(
                modifier = Modifier.align(End),
                text = token.totalValueUSD,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .align(End),
                text = "(${token.perTokenPriceUSD}/${token.symbol})",
                fontSize = 12.sp
            )
        }
    }
}
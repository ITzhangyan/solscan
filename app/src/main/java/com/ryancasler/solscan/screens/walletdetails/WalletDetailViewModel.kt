package com.ryancasler.solscan.screens.walletdetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryancasler.solscan.core.toCurrencyString
import com.ryancasler.solscan.network.models.*
import com.slack.eithernet.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class WalletDetailViewModel @Inject constructor(
    private val solScanApi: SolScanApi,
    private val coinGeckoApi: CoinGeckoApi
) : ViewModel() {

    private var address: String = ""

    val state: MutableState<WalletDetailState> = mutableStateOf(
        WalletDetailState.Loading("")
    )

    fun setUp(address: String) {
        this.address = address
        viewModelScope.launch {
            state.value = WalletDetailState.Loading(address)
            loadData(address)
        }
    }

    private suspend fun loadData(address: String) {
        val tokens = when (val result = solScanApi.getAccountTokens(address)) {
            is ApiResult.Success -> result.value
            is ApiResult.Failure -> {
                state.value = WalletDetailState.Error
                return
            }
        }

        val accountInfo = when (val result = solScanApi.getSolAccount(address)) {
            is ApiResult.Success -> result.value
            else -> {
                state.value = WalletDetailState.Error
                return
            }
        }

        val solData = when (val result = coinGeckoApi.getTokenMarket()) {
            is ApiResult.Success -> result.value
            else -> {
                state.value = WalletDetailState.Error
                return
            }
        }

        processTokens(tokens, accountInfo, solData.first())
    }

    private suspend fun processTokens(
        list: List<Token>,
        accountInfo: AccountDetails,
        solData: MarketDetails,
    ) {
        val solValue = accountInfo.solAmount * solData.current_price

        val tokensWithBalance = list.filter { it.tokenAmount.uiAmount > 0.0 }

        val fungibleTokens = tokensWithBalance.filter {
            it.tokenAmount.decimals > 0 && (it.priceUsdt ?: 0.0) > 0.0
        }

        val fungibleTokenUI = fungibleTokens
            .sortedBy { ((it.priceUsdt ?: 0.0) * it.tokenAmount.uiAmount) }
            .map {
            FungibleToken(
                it.tokenName,
                it.tokenSymbol ?: "",
                it.tokenAmount.uiAmount,
                ((it.priceUsdt ?: 0.0) * it.tokenAmount.uiAmount).toCurrencyString(),
                it.priceUsdt?.toCurrencyString() ?: "-",
                it.tokenIcon
            )
        }.toMutableList()
            .apply {
                add(
                    0, FungibleToken(
                        "Solana",
                        "SOL",
                        accountInfo.solAmount,
                        solValue.toCurrencyString(),
                        solData.current_price.toCurrencyString(),
                        solData.image
                    )
                )
            }
            .toList()

        val totalValue = solValue + fungibleTokens.sumOf {
            ((it.priceUsdt ?: 0.0) * it.tokenAmount.uiAmount)
        }

        val displayValue = totalValue.toCurrencyString()

        state.value = WalletDetailState.Loaded(shortAddress(), address, displayValue, fungibleTokenUI, solData)

        val nfts = tokensWithBalance.filter { it.tokenAmount.decimals == 0L }
        val loadedNfts = getNFTs(nfts)
        val nftState = if (nfts.isEmpty()) NftState.NoNfts else NftState.Loaded(loadedNfts)

        state.value =
            WalletDetailState.Loaded(shortAddress(), address, displayValue, fungibleTokenUI, solData, nftState)
    }

    private fun shortAddress(): String {
        if (address.length <= 8) return address
        return address.dropLast(address.length - 4) + "..." + address.takeLast(4)
    }

    private suspend fun getNFTs(
        nftList: List<Token>
    ): List<NFT> = coroutineScope {
        return@coroutineScope nftList.map { token ->
            async {
                (solScanApi.getNFTDetail(token.tokenAddress) as? ApiResult.Success)?.value
            }
        }.awaitAll()
            .filterNotNull()
            .filter {
                it.metadata != null
            }.map {
                NFT(
                    it.tokenInfo.name,
                    it.metadata?.data?.image ?: "",
                    it.account
                )
            }.sortedBy {
                it.name
            }
    }
}

data class NFT(
    val name: String,
    val previewUrl: String,
    val address: String
)

data class FungibleToken(
    val name: String,
    val symbol: String,
    val quantity: Double,
    val totalValueUSD: String,
    val perTokenPriceUSD: String,
    val imageUrl: String
)

sealed class WalletDetailState {
    data class Loading(
        val address: String
    ) : WalletDetailState()

    data class Loaded(
        val shortAddress: String,
        val accountAddress: String,
        val totalValueUSD: String,
        val tokens: List<FungibleToken>,
        val solanaDetails: MarketDetails,
        val nfts: NftState = NftState.Loading
    ) : WalletDetailState()

    object Error : WalletDetailState()
}

sealed class NftState {
    object Loading : NftState()
    object NoNfts : NftState()
    data class Loaded(val nfts: List<NFT>) : NftState()
}
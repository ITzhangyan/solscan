package com.ryancasler.solscan.screens.nftdetail

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryancasler.solscan.network.models.Attribute
import com.ryancasler.solscan.network.models.Collection
import com.ryancasler.solscan.network.models.Nft
import com.ryancasler.solscan.network.models.SolScanApi
import com.slack.eithernet.ApiResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NftDetailViewModel @Inject constructor(
    private val solScanApi: SolScanApi
): ViewModel() {

    val state: MutableState<DetailState> = mutableStateOf(DetailState.Loading)

    fun init(tokenAddress: String) {
        viewModelScope.launch {
            val nft = (solScanApi.getNFTDetail(tokenAddress) as? ApiResult.Success)?.value ?: return@launch

            state.value = DetailState.Loaded(nft.toUI())
        }
    }

    sealed class DetailState {
        object Loading : DetailState()
        data class Loaded(
            val nft: NFT
        ): DetailState()
    }

    data class NFT(
        val image: String,
        val collectionInfo: Collection,
        val name: String,
        val description: String,
        val mutable: Boolean,
        val attributes: List<Attribute>
    )

    private fun Nft.toUI() : NFT = NFT(
        metadata?.data?.image ?: "",
        metadata?.data?.collection ?: Collection("",""),
        tokenInfo.name,
        metadata?.data?.description ?: "",
        metadata?.isMutable == 1L,
        metadata?.data?.attributes ?: emptyList()
    )
}
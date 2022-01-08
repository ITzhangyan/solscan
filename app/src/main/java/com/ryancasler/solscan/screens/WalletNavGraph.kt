package com.ryancasler.solscan.screens

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ryancasler.solscan.screens.SolScanDestinations.buildNftDetailPath
import com.ryancasler.solscan.screens.SolScanDestinations.nftAddressArg
import com.ryancasler.solscan.screens.SolScanDestinations.walletAddressArg
import com.ryancasler.solscan.screens.addwallet.AddWalletScreen
import com.ryancasler.solscan.screens.addwallet.AddWalletViewModel
import com.ryancasler.solscan.screens.nftdetail.NftDetailScreen
import com.ryancasler.solscan.screens.nftdetail.NftDetailViewModel
import com.ryancasler.solscan.screens.walletdetails.WalletDetailViewModel
import com.ryancasler.solscan.screens.walletdetails.WalletScreen

object SolScanDestinations {
    const val addWallet = "add_wallet"

    const val walletAddressArg = "wallet_address"
    private const val walletDetailRoute = "wallet_detail"
    const val walletDetail = "${walletDetailRoute}/{$walletAddressArg}"
    fun buildWalletDetailPath(id: String) = "$walletDetailRoute/$id"

    const val nftAddressArg = "nft_address"
    private const val nftDetailRoute = "nft_detail"
    const val nftDetail = "$nftDetailRoute/{$nftAddressArg}"
    fun buildNftDetailPath(address: String) = "$nftDetailRoute/$address"
}

@ExperimentalMaterialApi
@Composable
fun SolScanNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = SolScanDestinations.addWallet,
    ) {
        composable(SolScanDestinations.addWallet) {
            AddWalletScreen(navController = navController)
        }

        composable(
            SolScanDestinations.walletDetail,
            arguments = listOf(navArgument(walletAddressArg) { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString(walletAddressArg) ?: ""
            val vm: WalletDetailViewModel = hiltViewModel()
            vm.setUp(address)
            WalletScreen(vm, navController)
        }

        composable(
            SolScanDestinations.nftDetail,
            arguments = listOf(navArgument(nftAddressArg) { type = NavType.StringType })
        ) { backStackEntry ->
            val address = backStackEntry.arguments?.getString(nftAddressArg) ?: ""
            val vm: NftDetailViewModel = hiltViewModel()
            vm.init(address)
            NftDetailScreen(vm)
        }
    }
}

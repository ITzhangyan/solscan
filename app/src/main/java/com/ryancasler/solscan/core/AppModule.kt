package com.ryancasler.solscan.core

import android.content.Context
import android.os.Build
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.ryancasler.solscan.network.models.CoinGeckoApi
import com.ryancasler.solscan.network.models.SolScanApi
import com.ryancasler.solscan.network.models.addDebugLogging
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cacheFile = File(context.applicationContext.cacheDir, "sol-cache").apply { mkdirs() }
        val cacheSize = (10 * 1024 * 1024).toLong() // 10 MB
        return Cache(cacheFile, cacheSize)
    }

    @Provides
    @Singleton
    fun provideSolScanApi(cache: Cache) = SolScanApi.buildClient(cache)

    @Provides
    @Singleton
    fun provideCoinGeckoApi(cache: Cache) = CoinGeckoApi.buildClient(cache)
}
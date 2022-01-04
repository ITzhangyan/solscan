package com.ryancasler.solscan.core

import android.app.Application
import android.os.Build
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.decode.SvgDecoder
import coil.util.CoilUtils
import com.ryancasler.solscan.network.models.addDebugLogging
import dagger.hilt.android.HiltAndroidApp
import okhttp3.OkHttpClient

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        val imageLoader = ImageLoader.Builder(this)
            .crossfade(true)
            .componentRegistry {
                add(SvgDecoder(this@App))
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(this@App))
                } else {
                    add(GifDecoder())
                }
            }
            .okHttpClient {
                OkHttpClient.Builder()
                    .addDebugLogging()
                    .cache(CoilUtils.createDefaultCache(this))
                    .build()
            }
            .build()

        Coil.setImageLoader(imageLoader)
    }
}
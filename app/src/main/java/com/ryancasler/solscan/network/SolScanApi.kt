package com.ryancasler.solscan.network.models

import com.google.gson.GsonBuilder
import com.ryancasler.solscan.BuildConfig
import com.slack.eithernet.ApiResult
import com.slack.eithernet.ApiResultCallAdapterFactory
import com.slack.eithernet.ApiResultConverterFactory
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.LoggingEventListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import retrofit2.http.Headers

interface SolScanApi {
    /**
     * Always need to update the wallet
     */
    @GET("account/tokens")
    @Headers("Cache-Control: no-cache")
    suspend fun getAccountTokens(
        @Query("account") account: String
    ): ApiResult<List<Token>, NetworkError>

    @GET("account/{account}")
    @Headers("Cache-Control: no-cache")
    suspend fun getSolAccount(
        @Path("account") address: String
    ): ApiResult<AccountDetails, NetworkError>

    @GET("account/{tokenId}")
    suspend fun getNFTDetail(
        @Path("tokenId") tokenId: String
    ): ApiResult<Nft, NetworkError>

    companion object {
        fun buildClient(cache: Cache): SolScanApi = retrofitBuilder()
            .baseUrl("https://public-api.solscan.io/")
            .client(defaultHttpClientBuilder(cache).build())
            .build()
            .create(SolScanApi::class.java)
    }
}

interface CoinGeckoApi {

    @GET("coins/markets")
    @Headers("Cache-Control: no-cache")
    suspend fun getTokenMarket(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("ids") ids: List<String> = listOf("solana")
    ): ApiResult<List<MarketDetails>, NetworkError>

    companion object {
        fun buildClient(cache: Cache): CoinGeckoApi = retrofitBuilder()
            .baseUrl("https://api.coingecko.com/api/v3/")
            .client(defaultHttpClientBuilder(cache).build())
            .build()
            .create(CoinGeckoApi::class.java)
    }
}

fun retrofitBuilder() = Retrofit.Builder()
    .addCallAdapterFactory(ApiResultCallAdapterFactory)
    .addConverterFactory(ApiResultConverterFactory)
    .addConverterFactory(
        GsonConverterFactory.create(GsonBuilder()
            .registerTypeAdapter(Attribute::class.java, AttributeDeserializer())
            .create())
    )

fun OkHttpClient.Builder.addDebugLogging(): OkHttpClient.Builder {
    if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        this.addInterceptor(loggingInterceptor)
        this.eventListenerFactory(LoggingEventListener.Factory())
    }

    return this
}

val cacheInterceptor: Interceptor = Interceptor { chain ->
    val response: Response = chain.proceed(chain.request())
    val maxAge = 60 * 60 * 24 * 3 // 3 days
    response.newBuilder()
        .header("Cache-Control", "public, max-age=$maxAge")
        .removeHeader("Pragma")
        .build()
}

fun defaultHttpClientBuilder(cache: Cache) = OkHttpClient()
    .newBuilder()
    .cache(cache)
    .addDebugLogging()
    .addNetworkInterceptor(cacheInterceptor)

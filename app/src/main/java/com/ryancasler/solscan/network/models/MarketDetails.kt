package com.ryancasler.solscan.network.models

//"https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&ids=solana"
//    "id":"solana",
//    "symbol":"sol",
//    "name":"Solana",
//    "image":"https://assets.coingecko.com/coins/images/4128/large/solana.png?1640133422",
//    "current_price":168.36,
//    "market_cap":52272480718,
//    "market_cap_rank":5,
//    "fully_diluted_valuation":null,
//    "total_volume":1362751344,
//    "high_24h":174.01,
//    "low_24h":166.61,
//    "price_change_24h":-2.213402566827,
//    "price_change_percentage_24h":-1.29764,
//    "market_cap_change_24h":198077593,
//    "market_cap_change_percentage_24h":0.38037,
//    "circulating_supply":309283730.875512,
//    "total_supply":508180963.57,
//    "max_supply":null,
//    "ath":259.96,
//    "ath_change_percentage":-35.18547,
//    "ath_date":"2021-11-06T21:54:35.825Z",
//    "atl":0.500801,
//    "atl_change_percentage":33544.36264,
//    "atl_date":"2020-05-11T19:35:23.449Z",
//    "roi":null,
//    "last_updated":"2022-01-04T23:55:03.253Z"

data class MarketDetails(
    val image: String,
    val current_price: Double,
    val price_change_percentage_24h: Double
)
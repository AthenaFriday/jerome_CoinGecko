package com.android.coingecko.model

import kotlinx.serialization.Serializable

@Serializable
data class TrendingResponse(
    val coins: List<TrendingCoin>
)

@Serializable
data class TrendingCoin(
    val item: TrendingItem
)

@Serializable
data class TrendingItem(
    val id: String,
    val coin_id: Int,
    val name: String,
    val symbol: String,
    val market_cap_rank: Int?,
    val thumb: String,
    val small: String,
    val large: String,
    val slug: String,
    val price_btc: Double,
    val score: Int
)

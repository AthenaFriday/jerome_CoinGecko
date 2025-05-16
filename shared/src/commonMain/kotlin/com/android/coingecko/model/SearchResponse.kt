package com.android.coingecko.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResponse(
    val coins: List<CoinItem>
)

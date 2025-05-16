package com.android.coingecko.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Exchange(
    val id: String,
    val name: String,
    val description: String,
    val url: String,
    val image: String,
    val country: String?,
    @SerialName("trade_volume_24h_btc") val tradeVolume24hBtc: Double, // ✅ used in UI
    @SerialName("trust_score_rank") val trustScoreRank: Int
)

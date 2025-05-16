package com.android.coingecko.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("category_id") val categoryId: String,
    val name: String
)

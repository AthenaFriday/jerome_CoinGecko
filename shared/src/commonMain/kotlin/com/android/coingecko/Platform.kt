package com.android.coingecko

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

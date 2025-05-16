package com.android.coingecko.api

import com.android.coingecko.model.Category
import com.android.coingecko.model.Exchange
import com.android.coingecko.model.SearchResponse
import com.android.coingecko.model.TrendingResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

private const val COINGECKO_API_KEY = "CG-73VEGJJXyWtXH3TpD3LxcVwf"

fun createCoinGeckoHttpClient(): HttpClient {
    return HttpClient {
        // JSON serialization
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }

        //  Inject headers globally
        install(DefaultRequest) {
            header("x-cg-pro-api-key", COINGECKO_API_KEY)
            accept(ContentType.Application.Json)
        }

        //  HTTP request/response logging
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }

        expectSuccess = true
    }
}

class CoinGeckoApi(private val client: HttpClient) {

    suspend fun getTrending(): TrendingResponse = client
        .get("https://api.coingecko.com/api/v3/search/trending")
        .body()

    suspend fun searchCoins(query: String): SearchResponse = client
        .get("https://api.coingecko.com/api/v3/search") {
            url {
                parameters.append("query", query)
            }
        }
        .body()

    suspend fun getExchanges(): List<Exchange> = client
        .get("https://api.coingecko.com/api/v3/exchanges")
        .body()

    suspend fun getCategories(): List<Category> = client
        .get("https://api.coingecko.com/api/v3/coins/categories/list")
        .body()
}

package com.android.coingecko.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.coingecko.api.CoinGeckoApi
import com.android.coingecko.api.createCoinGeckoHttpClient
import com.android.coingecko.model.Category
import com.android.coingecko.model.Exchange
import com.android.coingecko.model.SearchResponse
import com.android.coingecko.model.TrendingItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CryptoViewModel : ViewModel() {

    private val client = createCoinGeckoHttpClient()
    private val api = CoinGeckoApi(client)

    private val _trending = MutableStateFlow<List<TrendingItem>>(emptyList())
    val trending: StateFlow<List<TrendingItem>> = _trending

    private val _exchanges = MutableStateFlow<List<Exchange>>(emptyList())
    val exchanges: StateFlow<List<Exchange>> = _exchanges

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _searchResults = MutableStateFlow<SearchResponse?>(null)
    val searchResults: StateFlow<SearchResponse?> = _searchResults

    init {
        fetchTrendingCoins()
        fetchExchanges()
        fetchCategories()
    }

    fun fetchTrendingCoins() {
        viewModelScope.launch {
            try {
                println("Fetching from URL: https://api.coingecko.com/api/v3/search/trending")
                val result = api.getTrending()
                println("Fetched ${result.coins.size} trending coins")
                _trending.value = result.coins.map { it.item }
            } catch (e: Exception) {
                println("Error fetching trending coins: ${e.message}")
            }
        }
    }

    fun fetchExchanges() {
        viewModelScope.launch {
            try {
                println("Fetching from URL: https://api.coingecko.com/api/v3/exchanges")
                val result = api.getExchanges()
                println("Fetched ${result.size} exchanges")
                _exchanges.value = result
            } catch (e: Exception) {
                println("Error fetching exchanges: ${e.message}")
            }
        }
    }

    fun fetchCategories() {
        viewModelScope.launch {
            try {
                println("Fetching from URL: https://api.coingecko.com/api/v3/coins/categories/list")
                val result = api.getCategories()
                println("Fetched ${result.size} categories")
                _categories.value = result
            } catch (e: Exception) {
                println("Error fetching categories: ${e.message}")
            }
        }
    }

    fun searchCoins(query: String) {
        viewModelScope.launch {
            try {
                println("Searching from URL: https://api.coingecko.com/api/v3/search?query=$query")
                val result = api.searchCoins(query)
                println("Found ${result.coins.size} coins for query \"$query\"")
                _searchResults.value = result
            } catch (e: Exception) {
                println("Error searching coins: ${e.message}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        client.close()
    }
}

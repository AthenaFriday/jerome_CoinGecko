package com.android.coingecko.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Sync
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import coil.compose.AsyncImage
import com.android.coingecko.viewmodel.CryptoViewModel

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Trending : Screen("trending", "Trending", Icons.Filled.Star)
    object Search : Screen("search", "Search", Icons.Filled.Search)
    object Categories : Screen("categories", "Categories", Icons.Filled.List)
    object Exchanges : Screen("exchanges", "Exchanges", Icons.Filled.Sync)
}

@Composable
fun MainScreen(viewModel: CryptoViewModel = viewModel()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Trending.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Trending.route) { TrendingScreen(viewModel) }
            composable(Screen.Search.route) { SearchScreen(viewModel) }
            composable(Screen.Categories.route) { CategoriesScreen(viewModel) }
            composable(Screen.Exchanges.route) { ExchangesScreen(viewModel) }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val items = listOf(
        Screen.Trending,
        Screen.Search,
        Screen.Categories,
        Screen.Exchanges
    )
    BottomNavigation {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { screen ->
            BottomNavigationItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) }
            )
        }
    }
}

@Composable
fun TrendingScreen(viewModel: CryptoViewModel) {
    val trending by viewModel.trending.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchTrendingCoins()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Trending Coins", style = MaterialTheme.typography.h6)

        Spacer(modifier = Modifier.height(12.dp))

        if (trending.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(trending) { coin ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = 4.dp
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Name: ${coin.name}", style = MaterialTheme.typography.subtitle1)
                                Text("Symbol: ${coin.symbol}")
                                Text("Rank: ${coin.market_cap_rank ?: "N/A"}")
                            }
                            AsyncImage(
                                model = coin.thumb,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreen(viewModel: CryptoViewModel) {
    var query by remember { mutableStateOf("") }
    val results by viewModel.searchResults.collectAsState()

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(
            value = query,
            onValueChange = {
                query = it
                viewModel.searchCoins(query)
            },
            label = { Text("Search Coins") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        results?.coins?.forEach { result ->
            Text("• ${result.name} (${result.symbol})")
        }
    }
}

@Composable
fun CategoriesScreen(viewModel: CryptoViewModel) {
    val categories by viewModel.categories.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchCategories()
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(categories) { category ->
            Text("• ${category.name}", style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun ExchangesScreen(viewModel: CryptoViewModel) {
    val exchanges by viewModel.exchanges.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.fetchExchanges()
    }

    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
        items(exchanges) { exchange ->
            Column(Modifier.padding(vertical = 8.dp)) {
                Text("Name: ${exchange.name}", style = MaterialTheme.typography.subtitle1)
                Text("Country: ${exchange.country ?: "N/A"}")
                Text("Volume (BTC): ${exchange.tradeVolume24hBtc}")
            }
        }
    }
}



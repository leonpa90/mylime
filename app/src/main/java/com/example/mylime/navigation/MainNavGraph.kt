package com.example.mylime.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mylime.view.home.HomeViewModel
import com.example.mylime.view.home.HomeScreen
import com.example.mylime.view.detail.DetailScreen
import com.example.mylime.view.detail.DetailViewModel

enum class Routes(val value: String) {
    HOME("home"),
    DETAIL("detail")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    val context = LocalContext.current
    NavHost(navController = navController, startDestination = Routes.HOME.value) {
        composable(Routes.HOME.value) {
            val vm: HomeViewModel = hiltViewModel()
            val state = vm.state.collectAsState().value
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate("${Routes.DETAIL.value}/${id}")
                },
                beers = state.beers,
                isLoading = state.isLoading,
                isConnected = state.isConnected
            )
        }

        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val vm: DetailViewModel = hiltViewModel()
            LaunchedEffect(Unit) {
                it.arguments?.getInt("id")?.let { it1 -> vm.getDetail(it1) }
            }
            val state = vm.state.collectAsState().value
            DetailScreen(
                beer = state.beer,
                isLoading = state.isLoading,
                isConnected = state.isConnected,
                onShareClick = {vm.shareDetail(it, context)}
            )
        }
    }
}

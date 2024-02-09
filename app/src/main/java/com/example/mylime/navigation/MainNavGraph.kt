package com.example.mylime.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mylime.view.detail.DetailScreen
import com.example.mylime.view.home.HomeScreen

enum class Routes(val value: String) {
    HOME("home"),
    DETAIL("detail")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = Routes.HOME.value) {
        composable(Routes.HOME.value) {
            HomeScreen(
                onItemClick = { id ->
                    navController.navigate("${Routes.DETAIL.value}/${id}")
                },
            )
        }

        composable(
            route = "${Routes.DETAIL}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id")
            DetailScreen(id)
        }
    }
}

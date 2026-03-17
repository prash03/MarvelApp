
package com.example.marvelapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.marvelapp.presentation.SplashScreen
import com.example.marvelapp.presentation.details.CharacterDetailScreen
import com.example.marvelapp.presentation.list.CharacterListScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen {
                navController.navigate("list") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }

        composable("list") {
            CharacterListScreen { id ->
                navController.navigate("detail/$id")
            }
        }

        composable(
            route = "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            CharacterDetailScreen(characterId = id) {
                navController.popBackStack()
            }
        }
    }
}

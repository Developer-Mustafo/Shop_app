package uz.coder.shopapp.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import uz.coder.shopapp.screens.HomeScreen
import uz.coder.shopapp.screens.ShopItemScreen
import uz.coder.shopapp.screens.ShopScreen
import uz.coder.shopapp.screens.ShopSearchScreen
import uz.coder.shopapp.sealed.Screens

@Composable
fun ShopNavigation(modifier: Modifier) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screens.Home.route, modifier = modifier.fillMaxSize()){
        composable(Screens.Home.route){
            HomeScreen(navHostController = navController)
        }
        composable(Screens.ShopItem.route+"/{$ID}", arguments = listOf(
            navArgument(ID){ type = NavType.IntType }
        )){
            ShopItemScreen(navHostController = navController, it)
        }
        composable(Screens.ShopSearch.route){
            ShopSearchScreen(navHostController = navController)
        }
        composable(Screens.Shop.route+"/{$ID}/{$STATE}", arguments = listOf(
            navArgument(ID){ type = NavType.IntType },
            navArgument(STATE){ type = NavType.StringType })){
            ShopScreen(navHostController = navController,it)
        }
    }
}
const val ID = "id"
const val STATE = "state"
const val ADD = "add"
const val EDIT = "edit"
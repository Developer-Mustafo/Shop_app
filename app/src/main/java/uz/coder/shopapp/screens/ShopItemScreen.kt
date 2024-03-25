package uz.coder.shopapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uz.coder.shopapp.models.ShopItem
import uz.coder.shopapp.models.ShopItem.Companion.UNDEFINE_ID
import uz.coder.shopapp.navigation.ID
import uz.coder.shopapp.ui.theme.Main_Color
import uz.coder.shopapp.ui.theme.White
import uz.coder.shopapp.viewModel.ShopViewModel

@Composable
fun ShopItemScreen(navHostController: NavHostController, navBackStackEntry: NavBackStackEntry) {
    var getItem by remember {
        mutableStateOf(ShopItem(name = "", count = 0))
    }
    val id = navBackStackEntry.arguments?.getInt(ID) ?: UNDEFINE_ID
    val viewModel = viewModel<ShopViewModel>()
    val scope = rememberCoroutineScope()
    ShopItem(navHostController){
        scope.launch {
            if (id!= UNDEFINE_ID){
                viewModel.getById(id).collect{
                    getItem = it
                }
            }
        }
        getItem
    }
}

@Composable
fun ShopItem(navHostController: NavHostController, item:()->ShopItem) {
    val state = rememberScrollState()
    val shopItem = item()
    Column(modifier = Modifier
        .fillMaxSize()
        .background(Main_Color)
        .verticalScroll(state)) {
        Text(text = shopItem.name, fontSize = 25.sp, color = White)
        Text(text = shopItem.count.toString(), fontSize = 25.sp, color = White)
    }
}

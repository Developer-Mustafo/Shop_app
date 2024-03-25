package uz.coder.shopapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.models.ShopItem
import uz.coder.shopapp.models.ShopItem.Companion.UNDEFINE_ID
import uz.coder.shopapp.navigation.ADD
import uz.coder.shopapp.navigation.EDIT
import uz.coder.shopapp.navigation.ID
import uz.coder.shopapp.navigation.STATE
import uz.coder.shopapp.ui.theme.Main_Color
import uz.coder.shopapp.ui.theme.ShopAppTheme
import uz.coder.shopapp.viewModel.ShopViewModel

@Composable
fun ShopScreen(navHostController: NavHostController, navBackStackEntry: NavBackStackEntry) {
    val state = navBackStackEntry.arguments?.getString(STATE)?:ADD
    val id = navBackStackEntry.arguments?.getInt(ID)?: UNDEFINE_ID
    val viewModel = viewModel<ShopViewModel>()
    val scope = rememberCoroutineScope()
    var shopItem by remember {
        mutableStateOf(ShopItem(name = "", count = 0))
    }
    Shop(navHostController, viewModel, scope,state,id) {
        scope.launch {
            if (id != UNDEFINE_ID){
                viewModel.getById(id).collect {
                    shopItem = it
                }
            }
        }
        shopItem
    }

}

@Composable
fun Shop(
    navHostController: NavHostController,
    viewModel: ShopViewModel,
    scope: CoroutineScope,
    state: String,
    id: Int,
    shopItem: () -> ShopItem
) {
    val item = shopItem()
    var name by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableStateOf("")
    }
    when(state){
        ADD->{
            name = ""
            count = ""
        }
        EDIT->{
            name = item.name
            count = item.count.toString()
        }
    }
    ShopAppTheme {
        Surface {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                OutlinedTextField(value = name, onValueChange = { name = it }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp), label = { Text(text = stringResource(R.string.name)) })
                OutlinedTextField(value = count, onValueChange = { count = it }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp), label = { Text(text = stringResource(R.string.count)) })
                OutlinedButton(onClick = {
                    when(state){
                        ADD->{
                            scope.launch(Dispatchers.Main) {
                                val getItem = ShopItem(name = name.trim(), count = count.toInt())
                                Log.d(TAG, "Shop: $getItem")
                                viewModel.insert(shopItem = getItem)
                                navHostController.popBackStack()
                            }
                        }
                        EDIT->{
                            scope.launch(Dispatchers.Main) {
                                val getItem = ShopItem(id = id, name = name.trim(), count = count.toInt())
                                Log.d(TAG, "Shop: $getItem")
                                viewModel.update(shopItem = getItem)
                                navHostController.popBackStack()
                            }
                        }
                    }
                }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp), colors = ButtonDefaults.buttonColors(
                    Main_Color)) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(text = stringResource(id = R.string.save))
                    }
                }
            }
        }
    }
}
private const val TAG = "ShopScreen"
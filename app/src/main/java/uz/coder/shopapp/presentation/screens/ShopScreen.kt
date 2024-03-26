package uz.coder.shopapp.presentation.screens

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopItem.Companion.UNDEFINE_ID
import uz.coder.shopapp.domain.sealed.ResultState
import uz.coder.shopapp.domain.sealed.Screens
import uz.coder.shopapp.presentation.navigation.ADD
import uz.coder.shopapp.presentation.navigation.EDIT
import uz.coder.shopapp.presentation.navigation.ID
import uz.coder.shopapp.presentation.navigation.STATE
import uz.coder.shopapp.presentation.viewModel.ShopViewModel
import uz.coder.shopapp.ui.theme.Main_Color
import uz.coder.shopapp.ui.theme.ShopAppTheme

@Composable
fun ShopScreen(navHostController: NavHostController, navBackStackEntry: NavBackStackEntry) {
    val state = navBackStackEntry.arguments?.getString(STATE)?: ADD
    val id = navBackStackEntry.arguments?.getInt(ID)?: UNDEFINE_ID
    val viewModel = viewModel<ShopViewModel>()
    val scope = rememberCoroutineScope()
    var shopItem by remember {
        mutableStateOf(ShopItem(name = "", count = 0))
    }
    Shop(navHostController, viewModel, scope,state) {
        scope.launch {
            if (id != UNDEFINE_ID){
                viewModel.getById(id)
                viewModel.item.collect{
                    shopItem = it
                }
            }
        }
        shopItem
    }

}
    private val errorName = MutableStateFlow("")
    private val errorCount = MutableStateFlow("")
@Composable
fun Shop(
    navHostController: NavHostController,
    viewModel: ShopViewModel,
    scope: CoroutineScope,
    state: String,
    shopItem: () -> ShopItem
) {
    val context = LocalContext.current
    val item = shopItem()
    var name by remember {
        mutableStateOf("")
    }
    var count by remember {
        mutableStateOf("")
    }
    when(state){
        ADD ->{
            name = ""
            count = ""
        }
        EDIT ->{
            name = item.name
            count = item.count.toString()
        }
    }
    observer(viewModel,scope,navHostController)
    ShopAppTheme {
        Surface {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                OutlinedTextField(value = name, onValueChange = { name = it; viewModel.resetErrorInputName() }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp), label = { Text(text = stringResource(R.string.name)) })
                OutlinedTextField(value = count, onValueChange = { count = it; viewModel.resetErrorInputCount() }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp, end = 15.dp), label = { Text(text = stringResource(R.string.count)) })
                OutlinedButton(onClick = {
                    when(state){
                        ADD ->{
                            scope.launch(Dispatchers.Main) {
                                viewModel.insert(name.trim(), count.trim())
                            }
                        }
                        EDIT ->{
                            scope.launch(Dispatchers.Main) {
                                viewModel.update(name.trim(), count.trim())
                            }
                        }
                    }
                    Toast.makeText(context, errorName.value, Toast.LENGTH_SHORT).show()
                    Toast.makeText(context, errorCount.value, Toast.LENGTH_SHORT).show()
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

fun observer(viewModel: ShopViewModel, scope: CoroutineScope, navHostController: NavHostController) {
    scope.launch {
        viewModel.result.collect{
            when(it){
                is ResultState.ErrorName->{
                    if (it.enabled){
                       errorName.tryEmit(it.message)
                    }
                }
                is ResultState.ErrorCount->{
                    if (it.enabled){
                        errorCount.tryEmit(it.message)
                    }
                }
                is ResultState.Finish->{
                    navHostController.popBackStack(Screens.Home.route,
                        inclusive = false,
                        saveState = true
                    )
                }
            }
        }
    }
}

private const val TAG = "ShopScreen"
package uz.coder.shopapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.models.ShopItem
import uz.coder.shopapp.ui.theme.ShopAppTheme
import uz.coder.shopapp.viewModel.ShopViewModel

@Composable
fun ShopSearchScreen(navHostController: NavHostController) {
    val viewModel = viewModel<ShopViewModel>()
    ShopSearch(navHostController, viewModel)
}

@Composable
fun ShopSearch(navHostController: NavHostController, viewModel: ShopViewModel) {
    var find by remember {
        mutableStateOf("")
    }
    var list by remember {
        mutableStateOf(emptyList<ShopItem>())
    }
    val scope = rememberCoroutineScope()
    ShopAppTheme {
        Surface {
            Column {
                OutlinedTextField(value = find, onValueChange = { find = it }, leadingIcon = { Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null

                ) }, modifier = Modifier.padding(5.dp)
                    .fillMaxWidth(), label = { Text(
                    text = stringResource(id = R.string.search)
                ) })
                RecyclerView(navHostController, viewModel) {
                    viewModel.findList(find)
                    scope.launch {
                        viewModel.list.collect{
                            list = it
                        }
                    }
                    list
                }
            }
        }
    }
}

@Composable
fun RecyclerView(navHostController: NavHostController, viewModel: ShopViewModel,function: () -> List<ShopItem>) {
    LazyColumn(Modifier.fillMaxSize()) {
        itemsIndexed(function()){_,item->
            Items(item = item, navHostController = navHostController, viewModel = viewModel)
        }
    }
}

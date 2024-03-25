package uz.coder.shopapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.models.ShopItem
import uz.coder.shopapp.models.ShopItem.Companion.UNDEFINE_ID
import uz.coder.shopapp.navigation.ADD
import uz.coder.shopapp.navigation.EDIT
import uz.coder.shopapp.sealed.Screens
import uz.coder.shopapp.ui.theme.Black
import uz.coder.shopapp.ui.theme.Main_Color
import uz.coder.shopapp.ui.theme.ShopAppTheme
import uz.coder.shopapp.ui.theme.White
import uz.coder.shopapp.viewModel.ShopViewModel

@Composable
fun HomeScreen(navHostController: NavHostController) {
    val viewModel = viewModel<ShopViewModel>()
    val scope = rememberCoroutineScope()
    var list by remember {
        mutableStateOf(emptyList<ShopItem>())
    }
    viewModel.shopList()
    ShopAppTheme {
        Scaffold(floatingActionButton = {
            FabButton(navHostController)
        }) {
        Home(navHostController,Modifier.padding(it), viewModel = viewModel) {
            scope.launch {
                viewModel.list.collect {l->
                    list = l
                }
            }
            list
        }
        }
    }
}

@Composable
fun FabButton(navHostController: NavHostController) {
    FloatingActionButton(onClick = { navHostController.navigate(Screens.Shop.route+"/${UNDEFINE_ID}/$ADD") }, shape = FloatingActionButtonDefaults.smallShape, containerColor = Main_Color) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "", tint = Color.White)
    }
}

@Composable
fun Home(navHostController: NavHostController, modifier: Modifier, viewModel: ShopViewModel, list: () -> List<ShopItem>) {
    ShopAppTheme {
        Surface {
            Column(modifier = Modifier.fillMaxSize().padding(5.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                OutlinedTextField(value = "", onValueChange = {  }, leadingIcon = { Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null

                ) }, enabled = false, modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navHostController.navigate(Screens.ShopSearch.route) }, label = { Text(
                    text = stringResource(id = R.string.search)
                ) })
                LazyColumn(modifier = Modifier
                    .fillMaxSize()) {
                    itemsIndexed(list.invoke()) { _, item ->
                        Items(item, navHostController, viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Items(item: ShopItem, navHostController: NavHostController, viewModel: ShopViewModel) {
    val name = stringResource(id = R.string.name).lowerText().plus(": ${item.name}")
    val count = stringResource(id = R.string.count).lowerText().plus(": ${item.count}")
    var showDialog by remember { mutableStateOf(false) }
    ShopAppTheme {
        Surface {
            Card(onClick = { navHostController.navigate(Screens.ShopItem.route+"/${item.id}") }, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp), colors = CardDefaults.cardColors(Main_Color)) {
                Row(Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .weight(6f)) {
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = name, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                            .padding(start = 15.dp, end = 15.dp), color = White)
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(text = count, modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start)
                            .padding(start = 15.dp, end = 15.dp), color = White)
                        Spacer(modifier = Modifier.height(5.dp))
                    }
                    Column(modifier = Modifier
                        .width(50.dp)
                        .weight(1f)) {
                        IconButton(onClick = {
                            showDialog = true
                        }, modifier = Modifier.align(Alignment.End)) {
                            Icon(imageVector = Icons.Default.Delete, contentDescription = null, tint = Black)
                        }
                        IconButton(onClick = { navHostController.navigate(Screens.Shop.route+"/${item.id}/$EDIT")}, modifier = Modifier.align(Alignment.End)) {
                            Icon(imageVector = Icons.Default.Edit, contentDescription = null, tint = Black)
                        }
                    }
                }
            }
            if (showDialog) {
                AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = { Button(
                    onClick = {viewModel.delete(item.id); showDialog = false}) {
                    Text(text = stringResource(id = R.string.yes))
                } }, title = { Text(
                    text = stringResource(id = R.string.delete)
                ) }, text = { Text(text = stringResource(id = R.string.youWantToDelete)) })
            }
        }
    }
}
fun String.lowerText():String{
    val first = this.first().lowercase()
    val substring = this.subSequence(1, this.length)
    return first.plus(substring)
}
private const val TAG = "HomeScreen"
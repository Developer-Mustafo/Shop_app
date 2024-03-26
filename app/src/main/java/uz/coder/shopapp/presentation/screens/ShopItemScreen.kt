package uz.coder.shopapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopItem.Companion.UNDEFINE_ID
import uz.coder.shopapp.presentation.navigation.ID
import uz.coder.shopapp.presentation.viewModel.ShopViewModel
import uz.coder.shopapp.ui.theme.Black

@Composable
fun ShopItemScreen( navBackStackEntry: NavBackStackEntry) {
    var getItem by remember {
        mutableStateOf(ShopItem(name = "", count = 0))
    }
    val id = navBackStackEntry.arguments?.getInt(ID) ?: UNDEFINE_ID
    val viewModel = viewModel<ShopViewModel>()
    val scope = rememberCoroutineScope()
    ShopItem{
        scope.launch {
            if (id!= UNDEFINE_ID){
                viewModel.getById(id)
                viewModel.item.collect{
                    getItem = it
                }
            }
        }
        getItem
    }
}

@Composable
fun ShopItem(item:()-> ShopItem) {
    val state = rememberScrollState()
    val shopItem = item()
    Column(modifier = Modifier
        .fillMaxSize()
        .verticalScroll(state)) {
            Text(text = stringResource(id = R.string.name).lowerText().plus(": ${shopItem.name}"), fontSize = 25.sp, color = Black, fontFamily = FontFamily.Monospace)
            HorizontalDivider()
            Text(text = stringResource(id = R.string.count).lowerText().plus(": ${shopItem.count}"), fontSize = 25.sp, color = Black, fontFamily = FontFamily.Monospace)
    }
}

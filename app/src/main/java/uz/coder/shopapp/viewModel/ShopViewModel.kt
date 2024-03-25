package uz.coder.shopapp.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import uz.coder.shopapp.ShopRepositoryImpl
import uz.coder.shopapp.models.ShopItem

class ShopViewModel(application: Application):AndroidViewModel(application) {
    private val repository = ShopRepositoryImpl(application)
    suspend fun insert(shopItem: ShopItem){
        repository.insert(shopItem)
        shopList()
    }
    suspend fun update(shopItem: ShopItem){
        repository.update(shopItem)
        shopList()
    }
    private val _list = MutableSharedFlow<List<ShopItem>>()
    val list: SharedFlow<List<ShopItem>>
        get() = _list.asSharedFlow()
    fun shopList(){
        viewModelScope.launch {
            repository.getShopList().collect{
                _list.emit(it)
            }
        }
    }
    fun findList(find:String){
        viewModelScope.launch {
            repository.find(find).collect{
                _list.emit(it)
            }
        }
    }
    fun getById(id:Int) = repository.getById(id)
    fun delete(id: Int){
        viewModelScope.launch {
            repository.remove(id)
        }
        shopList()
    }
}
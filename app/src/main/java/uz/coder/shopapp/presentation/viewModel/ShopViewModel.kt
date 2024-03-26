package uz.coder.shopapp.presentation.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import uz.coder.shopapp.R
import uz.coder.shopapp.data.ShopRepositoryImpl
import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.sealed.ResultState
import uz.coder.shopapp.domain.useCase.AddShopItemUseCase
import uz.coder.shopapp.domain.useCase.DeleteShopItemUseCase
import uz.coder.shopapp.domain.useCase.FindShopItemListUseCase
import uz.coder.shopapp.domain.useCase.GetShopItemByIdUseCase
import uz.coder.shopapp.domain.useCase.GetShopItemListUseCase
import uz.coder.shopapp.domain.useCase.UpdateShopItemUseCase

class ShopViewModel(private val application: Application):AndroidViewModel(application) {
    private val repository = ShopRepositoryImpl(application)
    private val addShopItemUseCase = AddShopItemUseCase(repository)
    private val updateShopItemUseCase = UpdateShopItemUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val findShopItemListUseCase = FindShopItemListUseCase(repository)
    private val getByIdUseCase = GetShopItemByIdUseCase(repository)
    private val getShopItemListUseCase = GetShopItemListUseCase(repository)

    private val _list = MutableSharedFlow<List<ShopItem>>()
    val list: SharedFlow<List<ShopItem>>
        get() = _list.asSharedFlow()
    private val _item = MutableSharedFlow<ShopItem>()
    val item:SharedFlow<ShopItem>
        get() = _item.asSharedFlow()

    private val _result = MutableSharedFlow<ResultState>()
    val result:SharedFlow<ResultState>
        get() = _result.asSharedFlow()
    suspend fun insert(inputName:String?, inputCount:String?){
        val name = parseString(inputName)
        val count = parseInt(inputCount)
        val validateInput = validateInput(name,count)
        if (validateInput){
            viewModelScope.launch {
                Log.d(TAG, "insert: $validateInput")
                addShopItemUseCase(ShopItem(name = name, count = count))
            }
            finishWork(Unit)
            shopList()
        }
    }
    suspend fun update(inputName: String?, inputCount: String?){
        val name = parseString(inputName)
        val count = parseInt(inputCount)
        val validateInput = validateInput(name,count)
        if (validateInput){
            viewModelScope.launch {
                item.collect{
                    updateShopItemUseCase(it.copy(name = name, count = count))
                }
            }
            finishWork(Unit)
            shopList()
        }
    }
    private fun finishWork(u:Unit) {
        viewModelScope.launch {
            _result.emit(ResultState.Finish(u))
        }
    }

    private suspend fun validateInput(name: String, count: Int): Boolean {
        var result = true
            if (name.isBlank() || name ==""){
                _result.emit(ResultState.ErrorName(true, application.getString(R.string.errorName)))
                result = false
            }
            if (count<=0){
                _result.emit(ResultState.ErrorCount(true, application.getString(R.string.errorCount)))
                result = false
            }
        return result
    }

    private fun parseInt(str: String?) = try { str?.toInt()?:0 }catch (e:Exception){ 0 }
    private fun parseString(str: String?) = str?:""
    fun shopList(){
        viewModelScope.launch {
            getShopItemListUseCase().collect{
                _list.emit(it)
            }
        }
    }
    fun findList(find:String){
        viewModelScope.launch {
            findShopItemListUseCase(find).collect{
                _list.emit(it)
            }
        }
    }
    fun getById(id:Int){
        viewModelScope.launch {
            getByIdUseCase(id).collect{
                _item.emit(it)
            }
        }
    }
    fun delete(id: Int){
        viewModelScope.launch {
            deleteShopItemUseCase(id)
        }
        shopList()
    }
    fun resetErrorInputName() {
        viewModelScope.launch {
            _result.emit(ResultState.ErrorName(false, ""))
        }
    }
    fun resetErrorInputCount() {
        viewModelScope.launch {
            _result.emit(ResultState.ErrorCount(false, ""))
        }
    }
}

private const val TAG = "ShopViewModel"
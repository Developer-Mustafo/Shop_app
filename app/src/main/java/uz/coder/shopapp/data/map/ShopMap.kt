package uz.coder.shopapp.data.map

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import uz.coder.shopapp.data.db.ShopItemDbModel
import uz.coder.shopapp.domain.models.ShopItem

class ShopMap {
    fun shopToShopDbModel(shopItem: ShopItem) = ShopItemDbModel(id = shopItem.id, name = shopItem.name, count = shopItem.count)
    private fun shopDbModelToShop(shopItemDbModel: ShopItemDbModel) = ShopItem(id = shopItemDbModel.id, name = shopItemDbModel.name, count = shopItemDbModel.count)
    fun shopFlowToShopDbModelFlow(shopDbModel: Flow<ShopItemDbModel>)= flow<ShopItem> {
        shopDbModel.collect{
            emit(shopDbModelToShop(it))
        }
    }

    fun shopDbModelListFlowToShopItemListFlow(shopList: Flow<List<ShopItemDbModel>>)= flow<List<ShopItem>> {
        shopList.collect{ it ->
            val shopItems = it.map { shopDbModelToShop(it) }
            emit(shopItems)
        }
    }

}
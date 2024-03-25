package uz.coder.shopapp.models

import kotlinx.coroutines.flow.Flow

interface ShopRepository {
    suspend fun insert(shopItem: ShopItem)
    suspend fun update(shopItem: ShopItem)
    suspend fun remove(id:Int)
    fun getById(id: Int): Flow<ShopItem>
    fun find(find:String):Flow<List<ShopItem>>
    fun getShopList(): Flow<List<ShopItem>>
}
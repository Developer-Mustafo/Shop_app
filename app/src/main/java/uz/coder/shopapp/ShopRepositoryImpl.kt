package uz.coder.shopapp

import android.app.Application
import kotlinx.coroutines.flow.Flow
import uz.coder.shopapp.db.ShopDatabase
import uz.coder.shopapp.models.ShopItem
import uz.coder.shopapp.models.ShopRepository

class ShopRepositoryImpl(application: Application):ShopRepository {
    private val db = ShopDatabase.getDatabase(application).shopDao()
    override suspend fun insert(shopItem: ShopItem)  = db.insert(shopItem)

    override suspend fun update(shopItem: ShopItem)  = db.insert(shopItem)

    override suspend fun remove(id: Int)  = db.remove(id)

    override fun getById(id: Int): Flow<ShopItem>  = db.getById(id)

    override fun find(find: String): Flow<List<ShopItem>>  = db.find(find)

    override fun getShopList(): Flow<List<ShopItem>>  = db.getShopList()
}
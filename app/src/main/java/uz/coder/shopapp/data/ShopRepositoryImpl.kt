package uz.coder.shopapp.data

import android.app.Application
import kotlinx.coroutines.flow.Flow
import uz.coder.shopapp.data.db.ShopDatabase
import uz.coder.shopapp.data.map.ShopMap
import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopRepository

class ShopRepositoryImpl(application: Application): ShopRepository {
    private val db = ShopDatabase.getDatabase(application).shopDao()
    private val map = ShopMap()
    override suspend fun insert(shopItem: ShopItem)  = db.insert(map.shopToShopDbModel(shopItem))

    override suspend fun update(shopItem: ShopItem)  = db.insert(map.shopToShopDbModel(shopItem))

    override suspend fun remove(id: Int)  = db.remove(id)

    override fun getById(id: Int): Flow<ShopItem>  = map.shopFlowToShopDbModelFlow(db.getById(id))

    override fun find(find: String): Flow<List<ShopItem>>  = map.shopDbModelListFlowToShopItemListFlow(db.find(find))

    override fun getShopList(): Flow<List<ShopItem>>  = map.shopDbModelListFlowToShopItemListFlow(db.getShopList())
}
package uz.coder.shopapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uz.coder.shopapp.models.ShopItem
@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shopItem: ShopItem)
    @Query("delete from shopItem where id =:id")
    suspend fun remove(id:Int)
    @Query("select * from shopItem where id =:id limit 1")
    fun getById(id: Int):Flow<ShopItem>
    @Query("select * from shopItem where name like '%'||:find||'%'")
    fun find(find:String):Flow<List<ShopItem>>
    @Query("select * from shopItem order by id asc")
    fun getShopList(): Flow<List<ShopItem>>
}
package uz.coder.shopapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(shopItem: ShopItemDbModel)
    @Query("delete from shopItem where id =:id")
    suspend fun remove(id:Int)
    @Query("select * from shopItem where id =:id limit 1")
    fun getById(id: Int):Flow<ShopItemDbModel>
    @Query("select * from shopItem where name like '%'||:find||'%'")
    fun find(find:String):Flow<List<ShopItemDbModel>>
    @Query("select * from shopItem order by id asc")
    fun getShopList(): Flow<List<ShopItemDbModel>>
}
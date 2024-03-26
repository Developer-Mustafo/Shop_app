package uz.coder.shopapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import uz.coder.shopapp.domain.models.ShopItem

@Entity(tableName = "shopItem")
data class ShopItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int = ShopItem.UNDEFINE_ID,
    val name:String,
    val count:Int
)
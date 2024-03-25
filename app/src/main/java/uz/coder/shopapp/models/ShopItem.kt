package uz.coder.shopapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopItem")
data class ShopItem(
    @PrimaryKey(autoGenerate = true)
    val id:Int = UNDEFINE_ID,
    val name:String,
    val count:Int
) {
    companion object{
        const val UNDEFINE_ID = 0
    }
}
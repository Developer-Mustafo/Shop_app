package uz.coder.shopapp.data.db

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.coder.shopapp.domain.models.ShopItem

@Database(entities = [ShopItemDbModel::class], version = 1)
abstract class ShopDatabase:RoomDatabase() {
    abstract fun shopDao(): ShopDao
    companion object{
        private var shopDatabase: ShopDatabase? = null
        private const val DB_NAME = "shop_db.db"
        private val LOCK = Any()
        fun getDatabase(application: Application): ShopDatabase {
            shopDatabase?.let {
                return it
            }
            synchronized(LOCK){
                shopDatabase?.let {
                    return it
                }
            }
            val db = Room.databaseBuilder(application, ShopDatabase::class.java, DB_NAME).fallbackToDestructiveMigration().build()
            shopDatabase = db
            return db
        }
    }
}
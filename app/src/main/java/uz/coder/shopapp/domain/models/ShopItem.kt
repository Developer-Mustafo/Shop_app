package uz.coder.shopapp.domain.models

data class ShopItem(
    val id:Int = UNDEFINE_ID,
    val name:String,
    val count:Int
) {
    companion object{
        const val UNDEFINE_ID = 0
    }
}
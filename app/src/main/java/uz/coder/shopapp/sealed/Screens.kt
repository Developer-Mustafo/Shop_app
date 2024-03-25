package uz.coder.shopapp.sealed

sealed class Screens(val route:String) {
    data object Home:Screens(HOME)
    data object ShopSearch:Screens(SHOP_SEARCH)
    data object ShopItem:Screens(SHOP_ITEM)
    data object Shop:Screens(SHOP)
    companion object{
        private const val HOME = "home"
        private const val SHOP_SEARCH = "shop_search"
        private const val SHOP_ITEM = "shop_item"
        private const val SHOP = "shop"
    }
}
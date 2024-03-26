package uz.coder.shopapp.domain.useCase

import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopRepository

class AddShopItemUseCase(private val repository: ShopRepository) {
    suspend operator fun invoke(shopItem: ShopItem) = repository.insert(shopItem)
}
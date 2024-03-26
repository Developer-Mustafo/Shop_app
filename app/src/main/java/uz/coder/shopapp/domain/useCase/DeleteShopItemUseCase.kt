package uz.coder.shopapp.domain.useCase

import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopRepository

class DeleteShopItemUseCase(private val repository: ShopRepository) {
    suspend operator fun invoke(id:Int) = repository.remove(id)
}
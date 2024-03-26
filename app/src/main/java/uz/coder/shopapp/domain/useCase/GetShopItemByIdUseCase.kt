package uz.coder.shopapp.domain.useCase

import uz.coder.shopapp.domain.models.ShopItem
import uz.coder.shopapp.domain.models.ShopRepository

class GetShopItemByIdUseCase(private val repository: ShopRepository) {
    operator fun invoke(id:Int) = repository.getById(id)
}
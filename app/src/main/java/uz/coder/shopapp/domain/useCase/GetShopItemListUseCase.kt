package uz.coder.shopapp.domain.useCase

import uz.coder.shopapp.domain.models.ShopRepository

class GetShopItemListUseCase(private val repository: ShopRepository) {
    operator fun invoke() = repository.getShopList()
}
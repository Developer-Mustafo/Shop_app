package uz.coder.shopapp.domain.useCase

import uz.coder.shopapp.domain.models.ShopRepository

class FindShopItemListUseCase(private val repository: ShopRepository) {
    operator fun invoke(find:String) = repository.find(find)
}
package org.smartjava.kotlin.itemservice.service

import arrow.Kind
import arrow.effects.ForFluxK
import arrow.effects.ForMonoK
import arrow.effects.monok.monad.binding
import org.smartjava.kotlin.itemservice.db.ItemRepository
import org.smartjava.kotlin.itemservice.model.Item
import java.util.*

interface ItemService<F, S> {

    fun getItem(itemId: UUID): Kind<F, Item>
    fun createItem(order: Item): Kind<F, Item>
    fun getAllItems(): Kind<F, List<Item>>
    fun updateItemIExistsAndReturnAllItems(toUpdate: UUID, description: String): Kind<F, List<Item>>
}

class ReactiveItemService(val itemRepository: ItemRepository<ForMonoK, ForFluxK>) : ItemService<ForMonoK, ForFluxK> {

    override fun updateItemIExistsAndReturnAllItems(toUpdate: UUID, description: String): Kind<ForMonoK, List<Item>> = binding {
        val existingItem = itemRepository.getItem(toUpdate).bind()
        val updatedItem = existingItem.copy(description = description)
        val storedItem = itemRepository.storeItem(updatedItem).bind()

        itemRepository.getAllItems().bind()
    }

    override fun getAllItems(): Kind<ForMonoK, List<Item>> = itemRepository.getAllItems()
    override fun createItem(item: Item): Kind<ForMonoK, Item> = itemRepository.storeItem(item)
    override fun getItem(itemId: UUID): Kind<ForMonoK, Item> = itemRepository.getItem(itemId)
}


package org.smartjava.kotlin.itemservice.db

import arrow.Kind
import arrow.effects.ForFluxK
import arrow.effects.ForMonoK
import org.smartjava.kotlin.itemservice.db.StoreUtil.errorIfEmpty
import org.smartjava.kotlin.itemservice.db.StoreUtil.mapToError
import org.smartjava.kotlin.itemservice.model.Item
import org.springframework.data.mongodb.core.ReactiveMongoOperations
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import java.util.*

class ReactiveItemRepository(val mongoTemplate: ReactiveMongoTemplate) : ItemRepository<ForMonoK, ForFluxK>, ReactiveMongoOperations by mongoTemplate {

    val ITEM_COLLECTION_NAME = "items"

    override fun getItem(itemId: UUID): Kind<ForMonoK, Item> =
        StoreUtil.asMono {
            findById(itemId, Item::class.java, ITEM_COLLECTION_NAME)
                .errorIfEmpty(ITEM_COLLECTION_NAME, itemId.toString())
        }

    override fun getAllItems(): Kind<ForMonoK, List<Item>> =
        StoreUtil.asMono {
            findAll(Item::class.java, ITEM_COLLECTION_NAME)
                .collectList()
        }

    override fun storeItem(order: Item): Kind<ForMonoK, Item> =
        StoreUtil.asMono {
            insert(order, ITEM_COLLECTION_NAME)
                .mapToError(ITEM_COLLECTION_NAME, order.id.toString())
        }

    override fun getAllItemsStreaming(): Kind<ForFluxK, Item> =
        StoreUtil.asFlux {
            findAll(Item::class.java, ITEM_COLLECTION_NAME)
        }
}


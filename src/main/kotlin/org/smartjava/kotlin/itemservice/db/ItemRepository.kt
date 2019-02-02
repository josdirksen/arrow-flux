package org.smartjava.kotlin.itemservice.db

import arrow.Kind
import org.smartjava.kotlin.itemservice.model.Item
import java.util.*

/**
 * @param F Wrapper for the single results e.g a ForMonoK or a ForId
 * @param S Wrapper for the streaming results e.g a ForFluxK or a ForListK
 */
interface ItemRepository<F, S> {

    fun storeItem(item: Item): Kind<F, Item>
    fun getItem(itemId: UUID): Kind<F, Item>
    fun getAllItems(): Kind<F, List<Item>>
    fun getAllItemsStreaming(): Kind<S, Item>
}

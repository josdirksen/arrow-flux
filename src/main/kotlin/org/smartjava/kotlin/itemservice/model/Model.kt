package org.smartjava.kotlin.itemservice.model

import arrow.core.Option
import arrow.instances.option.monad.binding
import java.util.*

data class Item(val id: UUID = UUID.randomUUID(), val name: String, val description: String)

object pp {
    init {

        val optionUUID: Option<UUID> = Option.just(UUID.randomUUID())
        val optionName: Option<String> = Option.just("")
        val optionDescription: Option<String> = Option.just("")

        val optionItem = optionUUID.flatMap { uuid ->
            optionName.flatMap { name ->
                optionDescription.map { desc ->
                    Item(uuid, name, desc)
                }
            }
        }

        val optionItem2 = binding {
            val uuid = optionUUID.bind()
            val name = optionName.bind()
            val description = optionName.bind()

            Item(uuid, name, description)
        }
    }
}

/**
 * Contains set of errors that can be thrown by the service. We need to determine whether
 * we're going to use the nullcheck from kotlin, or wrap it in an Option. For now lets see
 * if the kotlin approach is flexible enough. There is probably no need to sequence these
 * optional values, so using '?' should be ok.
 */
object Errors {

    class PropertyValidationException(invalidProperty: String,
                                      invalidValue: String? = null,
                                      override val message: String? = null,
                                      override val cause: Throwable? = null) : Throwable()

    class ObjectAlreadyExistsException(objectType: String,
                                       objectId: String = "",
                                       override val message: String? = "Object of type: $objectType, already exists for value: $objectId",
                                       override val cause: Throwable?) : Throwable()

    class ObjectNotFoundException(objectType: String,
                                       objectId: String = "",
                                       override val message: String? = "Object of type: $objectType, not found for value: $objectId",
                                       override val cause: Throwable? = null) : Throwable()
}
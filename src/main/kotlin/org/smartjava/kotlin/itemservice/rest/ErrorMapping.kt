package org.smartjava.kotlin.itemservice.rest

import org.smartjava.kotlin.itemservice.model.Errors
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

object ErrorMapping {

    val mappings: MutableMap<Class<*>, (Throwable) -> ResponseStatusException> = mutableMapOf()

    fun registerMapping(p: Class<*>, mapTo: (Throwable) -> ResponseStatusException) = mappings.put(p, mapTo)

    fun registerExceptions() {
        registerMapping(Errors.PropertyValidationException::class.java) { t -> ResponseStatusException(HttpStatus.BAD_REQUEST, t.message, t) }
        registerMapping(Errors.ObjectAlreadyExistsException::class.java) { t ->
            ResponseStatusException(
                HttpStatus.CONFLICT,
                t.message,
                t
            )
        }
        registerMapping(Errors.ObjectNotFoundException::class.java) { t ->
            ResponseStatusException(
                HttpStatus.NOT_FOUND,
                t.message,
                t
            )
        }
    }
}



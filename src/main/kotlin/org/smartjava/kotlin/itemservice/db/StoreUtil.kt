package org.smartjava.kotlin.itemservice.db

import arrow.effects.*
import org.smartjava.kotlin.itemservice.model.Errors
import org.springframework.dao.DuplicateKeyException
import arrow.effects.monok.monadThrow.bindingCatch as monokBindingCatch
import arrow.effects.fluxk.monadThrow.bindingCatch as fluxkBindingCatch

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

/**
 * Some utils to make working with the Mono and MonoK stuff easier in the
 * database layer.
 */
object StoreUtil {

    /**
     * Mono extension which throws an exception if an object isn't found
     */
    fun <R> Mono<R>.errorIfEmpty(objectType: String, objectId: String): Mono<R> {
        return this.switchIfEmpty(Mono.defer { Mono.error<R>(
            Errors.ObjectNotFoundException(
                objectType = objectType,
                objectId = objectId)) })
    }

    fun <R> Flux<R>.errorIfEmpty(objectType: String, objectId: String): Flux<R> {
        return this.switchIfEmpty(Mono.defer { Mono.error<R>(
            Errors.ObjectNotFoundException(
                objectType = objectType,
                objectId = objectId)) })
    }

    /**
     * Mono extensions to map supported standard mongo errors to our own domain
     */
    fun <R> Mono<R>.mapToError(objectType: String, objectId: String = "undefined") = this.onErrorMap {
        when (it) {
            is DuplicateKeyException -> Errors.ObjectAlreadyExistsException(
                    objectType = objectType,
                    objectId = objectId,
                    cause = it.cause)
            else -> it
        }
    }

    /**
     * Wrap a Mono<T> in a MonoK<T> using a bindingCatch. This prevents
     * errors bubbling up directly from the thunk to the onError subscriber, and allows
     * us to use monad comprehensions on the resulting MonoK<T>
     */
    fun <T>asMono(thunk: () -> Mono<T>): MonoK<T> {
        return monokBindingCatch {
            thunk().k().bind()
        }
    }

    /**
     * Wrap a Flux<T> in a FluxK<T> using a bindingCatch. This prevents
     * errors bubbling up directly from the thunk to the onError subscriber, and allows
     * us to use monad comprehensions on the resulting FluxK<T>
     */
    fun <T>asFlux(thunk: () -> Flux<T>): FluxK<T> {
        return fluxkBindingCatch {
            thunk().k().bind()
        }
    }
}

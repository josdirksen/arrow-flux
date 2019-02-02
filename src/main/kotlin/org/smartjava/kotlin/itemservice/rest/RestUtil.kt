package org.smartjava.kotlin.itemservice.rest

import arrow.effects.MonoK
import arrow.effects.k
import org.springframework.web.reactive.function.server.ServerRequest
import reactor.core.publisher.Mono
import java.util.*
import org.smartjava.kotlin.itemservice.model.Errors.PropertyValidationException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.ServerResponse

object RestUtil {

    fun pathParamToUUID(request: ServerRequest, param: String): MonoK<UUID> = Mono
            .just(request.pathVariable(param))
            .map { UUID.fromString(it) }
            .onErrorMap { t -> PropertyValidationException(
                invalidProperty = param,
                cause = t,
                message = "Parameter {$param} can't be converted to a UUID")
            }.k()

    inline fun <reified T>okJson(p: () -> MonoK<T>) = ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters
            .fromPublisher(p().mono, T::class.java))

    inline fun <reified T>okJsonList(p: () -> MonoK<List<T>>) : Mono<ServerResponse>  {
        val pType = object: ParameterizedTypeReference<List<T>>() {}
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(BodyInserters.fromPublisher(p().mono, pType))
    }
}


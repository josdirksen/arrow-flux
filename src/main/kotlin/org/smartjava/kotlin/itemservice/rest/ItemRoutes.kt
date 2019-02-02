package org.smartjava.kotlin.itemservice.rest

import arrow.effects.*
import arrow.effects.monok.monad.binding
import org.smartjava.kotlin.itemservice.Config.BASE_ROUTE
import org.smartjava.kotlin.itemservice.model.Item
import org.smartjava.kotlin.itemservice.service.ItemService
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono
import org.smartjava.kotlin.itemservice.rest.RestUtil.pathParamToUUID
import org.smartjava.kotlin.itemservice.rest.RestUtil.okJson
import org.smartjava.kotlin.itemservice.rest.RestUtil.okJsonList

object ItemsRoute {

    const val ITEMS_PATH = "items"
    const val PATH_ID = "id"

    class ItemRoutes(itemsRouteHandler: ItemsRouteHandler) {

        val route: RouterFunction<ServerResponse> = router {

            (path("$BASE_ROUTE/$ITEMS_PATH") and accept(APPLICATION_JSON)).nest {
                POST("", itemsRouteHandler::createOrder)
                GET("", itemsRouteHandler::getAllOrders)
                GET("/{$PATH_ID}", itemsRouteHandler::getOrder)
            }
        }
    }

    class ItemsRouteHandler(val itemService: ItemService<ForMonoK, ForFluxK>) {

        fun getAllOrders(request: ServerRequest): Mono<ServerResponse> = okJsonList {
            itemService.getAllItems().fix()
        }

        fun getOrder(request: ServerRequest): Mono<ServerResponse> = okJson {
            binding {
                val uuid = pathParamToUUID(request, PATH_ID).bind()
                itemService.getItem(uuid).bind()
            }
        }

        fun createOrder(request: ServerRequest): Mono<ServerResponse> = okJson {
            binding {
                val item = request.bodyToMono<Item>().k().bind()
                itemService.createItem(item).bind()
            }
        }
    }
}




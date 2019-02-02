package org.smartjava.kotlin.itemservice

import org.slf4j.LoggerFactory
import org.springframework.context.support.GenericApplicationContext
import org.springframework.http.server.reactive.HttpHandler
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter
import org.springframework.web.server.adapter.WebHttpHandlerBuilder
import reactor.netty.http.server.HttpServer
import java.time.Duration

/**
 * Simple Application to start a Flux server using Kotlin.
 */
class Application {

    val log = LoggerFactory.getLogger(Application::class.java)

    private val httpHandler: HttpHandler
    private val server: HttpServer

    /**
     * Setup the http server. Delegates to the bean named "webHandler" defined by
     * the beans function.
     */
    constructor() {

        // setup the context containing all our spring stuff
        val context = GenericApplicationContext().apply {
            allBeans().initialize(this)
            refresh()
        }
        server = HttpServer.create().port(Config.SERVER_PORT)

        // create the http handler, and assign filters
        httpHandler = WebHttpHandlerBuilder
                .applicationContext(context)
                .build()
    }

    fun startAndAwait() {
        server.handle(ReactorHttpHandlerAdapter(httpHandler))
            .bindUntilJavaShutdown(Duration.ofSeconds(Config.SERVER_SHUTDOWN_TIMEOUT)) { t ->
            log.info("Server Started")
        }
    }
}

/**
 * Starts the service, and waits until shutdown signal is received.
 */
fun main(args: Array<String>) {
    Application().startAndAwait()
}
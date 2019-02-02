package org.smartjava.kotlin.itemservice

import com.typesafe.config.ConfigFactory

/**
 * Exposes the configuration in the application.conf file.
 */
object Config {
    val config = ConfigFactory.load()

    // webserver settings
    val SERVER_PORT = config.getInt("server.port")
    val SERVER_SHUTDOWN_TIMEOUT = config.getLong("server.shutdown-timeout")

    // Database settings
    val DATABASE_NAME = config.getString("store.database-name")
    val DATABASE_HOST = config.getString("store.database-host")
    val DATABASE_PORT = config.getInt("store.database-port")

    // rest stuff
    val BASE_ROUTE = config.getString("route.base-route")
}

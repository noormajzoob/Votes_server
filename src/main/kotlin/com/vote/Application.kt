package com.vote

import com.vote.data.DatabaseFactory
import io.ktor.server.application.*
import com.vote.plugins.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)


@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {
    DatabaseFactory.init()
    configureSecurity()
    configureSerialization()
    configureMonitoring()
    configureRouting()
}

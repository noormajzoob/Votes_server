package com.vote.plugins

import com.vote.presentation.routes.userRoute
import com.vote.presentation.routes.voteRoute
import io.ktor.server.routing.*
import io.ktor.server.response.*
import io.ktor.server.application.*

fun Application.configureRouting() {
    routing {
        userRoute()
        voteRoute()
    }
}

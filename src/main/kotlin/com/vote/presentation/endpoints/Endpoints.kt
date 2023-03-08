package com.vote.presentation.endpoints

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

inline fun <reified T: Any> Route.getRoute(route: String = "/{id}", crossinline source: suspend (id: Long)-> T?){
    get(route){
        try {
            val id = call.parameters["id"]?.toLong()!!
            val data = source(id)

            if (data != null)
                call.respond(HttpStatusCode.OK, data)
            else call.respond(HttpStatusCode.NotFound)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

inline fun <reified T: Any> Route.postRoute(route: String = "/", crossinline source: suspend (data: T)-> T?){
    post(route){
        try {
            val body = call.receive<T>()
            val data = source(body)

            if (data != null)
                call.respond(HttpStatusCode.OK, data)
            else call.respond(HttpStatusCode.BadRequest)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

inline fun <reified T: Any> Route.postAnyRoute(route: String = "/", crossinline source: suspend (data: T)-> Any?){
    post(route){
        try {
            val body = call.receive<T>()
            val data = source(body)

            if (data != null)
                call.respond(HttpStatusCode.OK, data)
            else call.respond(HttpStatusCode.BadRequest)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

inline fun <reified T: Any> Route.deleteRoute(crossinline source: suspend (id: Long)-> T?){
    delete("/{id}"){
        try {
            val id = call.parameters["id"]?.toLong()!!
            val data = source(id)

            if (data != null)
                call.respond(HttpStatusCode.OK, data)
            else call.respond(HttpStatusCode.NotFound)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

inline fun <reified T: Any> Route.putRoute(route: String = "/{id}", crossinline source: suspend (id: Long, data: T)-> Any?){
    put(route){
        try {
            val id = call.parameters["id"]?.toLong()!!
            val body = call.receive<T>()
            val data = source(id, body)

            if (data != null)
                call.respond(HttpStatusCode.OK, data)
            else call.respond(HttpStatusCode.NotFound)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}

inline fun <reified T: Any> Route.getAllRoute(route: String = "/list", crossinline source: suspend (offset: Long, limit: Int, id: Long?)-> List<T>?){
    get(route){
        try {
            val offset = call.request.rawQueryParameters["offset"]?.toLong() ?: 0
            val limit = call.request.rawQueryParameters["limit"]?.toInt() ?: Int.MAX_VALUE

            val id = call.parameters["id"]?.toLong()
            val data = source(offset, limit, id)

            if (data != null)
                call.respond(HttpStatusCode.OK,
                    mapOf(
                        "offset" to offset,
                        "limit" to if (limit == Int.MAX_VALUE) 0 else limit,
                        "next_offset" to if (limit == Int.MAX_VALUE) 0 else offset + limit,
                        "page_size" to data.size,
                        "data" to data
                    )
                )
            else call.respond(HttpStatusCode.NotFound)
        }catch (e: Exception){
            e.printStackTrace()
            call.respond(HttpStatusCode.BadRequest)
        }
    }
}
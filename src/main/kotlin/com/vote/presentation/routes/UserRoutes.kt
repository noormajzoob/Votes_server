package com.vote.presentation.routes

import com.vote.data.util.EmailService
import com.vote.data.util.JwtVerifier
import com.vote.domain.dao.UserDao
import com.vote.domain.model.User
import com.vote.presentation.endpoints.*
import com.vote.presentation.endpoints.postRoute
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Routing.userRoute(){
    val dao = UserDao()

    route("/user"){
        postRoute<User> { body ->
            val user = dao.addUser(
                name = body.name!!,
                password = body.password!!,
                email = body.email!!
            )

            user
        }

        postAnyRoute<User>("/login"){ user ->
            val isValid = dao.login(user.email!!, user.password!!)

            if (isValid)
                JwtVerifier.generate(user.email, user.password)
            else null
        }

        authenticate("auth"){
            getRoute { id ->
                dao.getUser(id)
            }

            putRoute<User> { id, user ->

                null
            }

            deleteRoute { id ->

            }
        }
    }
}
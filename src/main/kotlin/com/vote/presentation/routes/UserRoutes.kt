package com.vote.presentation.routes

import com.vote.data.util.JwtVerifier
import com.vote.domain.dao.UserDao
import com.vote.domain.model.User
import com.vote.presentation.dto.LoginRepoDto
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

        postAnyRoute<User>("/login"){ body ->
            val isValid = dao.login(body.email!!, body.password!!)

            if (isValid) {
                val user = dao.getUser(body.email!!)
                val token = JwtVerifier.generate(user.email!!)

                LoginRepoDto(
                    user!!,
                    token
                )
            }else null
        }

        authenticate("auth"){

            putRoute<User> { id, user ->

                null
            }

            deleteRoute { id ->

            }
        }
    }
}
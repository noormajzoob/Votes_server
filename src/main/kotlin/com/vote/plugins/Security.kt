package com.vote.plugins

import io.ktor.server.response.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import com.vote.data.util.JwtVerifier
import com.vote.domain.dao.UserDao
import io.ktor.http.*
import io.ktor.server.application.*

fun Application.configureSecurity() {
    
    authentication {
            jwt("auth") {
                realm = JwtVerifier.realm()
                verifier(JwtVerifier.verifier())
                validate { credential ->
                    if (credential.payload.audience.contains(JwtVerifier.audience())) JWTPrincipal(credential.payload) else null
                }
                challenge { defaultScheme, realm ->
                    call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
                }
            }
        }
}

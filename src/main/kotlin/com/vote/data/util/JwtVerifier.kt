package com.vote.data.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import java.util.*

object JwtVerifier {

    private lateinit var secret: String
    private lateinit var audience: String
    private lateinit var issuer: String
    private lateinit var realm: String

    fun init(config: ApplicationConfig){
        secret = config.property("jwt.secret").getString()
        issuer = config.property("jwt.issuer").getString()
        audience = config.property("jwt.audience").getString()
        realm = config.property("jwt.realm").getString()
    }

    fun verifier(): JWTVerifier{
        return JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()
    }

    fun audience() = audience
    fun realm() = realm

    fun generate(email: String): String{
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + (5 * 60 * 1000)))
            .sign(Algorithm.HMAC256(secret))
    }
}
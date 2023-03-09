package com.vote.data.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtVerifier {

    private val secret = System.getenv("secret")
    private val jwtAudience = System.getenv("jwt.audience")
    private val issuer = System.getenv("jwt.issuer")
    private val realm = System.getenv("jwt.realm")


    fun verifier(): JWTVerifier{
        return JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(jwtAudience)
            .withIssuer(issuer)
            .build()
    }

    fun audience() = jwtAudience
    fun realm() = realm

    fun generate(email: String): String{
        return JWT.create()
            .withAudience(jwtAudience)
            .withIssuer(issuer)
            .withClaim("email", email)
            .withExpiresAt(Date(System.currentTimeMillis() + (5 * 60 * 1000)))
            .sign(Algorithm.HMAC256(secret))
    }
}
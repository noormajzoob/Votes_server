package com.vote.data.util

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import java.util.*

object JwtVerifier {

    private lateinit var secret: String
    private lateinit var jwtAudience: String
    private lateinit var issuer: String
    private lateinit var realm: String

    fun init(){
        secret = System.getenv("secret")
        jwtAudience = System.getenv("jwt.audience")
        issuer = System.getenv("jwt.issuer")
        realm = System.getenv("jwt.realm")
    }

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
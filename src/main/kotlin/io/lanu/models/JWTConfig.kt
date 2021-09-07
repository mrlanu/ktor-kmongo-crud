package io.lanu.models

import io.ktor.application.*

data class JWTConfig(private val environment: ApplicationEnvironment){
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()
}

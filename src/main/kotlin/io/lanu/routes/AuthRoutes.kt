package io.lanu.routes

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.lanu.dao.User
import io.lanu.exceptions.ResourceNotFoundException
import io.lanu.models.LoginRequest
import io.lanu.services.IAuthService
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Route.authRoutes(environment: ApplicationEnvironment){

    val authService: IAuthService by inject()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    val myRealm = environment.config.property("jwt.realm").getString()

    route("") {
        post("/register"){
            val registerRequest = call.receive<LoginRequest>()
            authService.registerUser(registerRequest)
            call.respondText("User has been registered", status = HttpStatusCode.Created)
        }
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            // Check username and password
            val user = authService.findOneByUsername(loginRequest)
            if ((user == null) || !BCrypt.checkpw(loginRequest.password, user.pass)){
                call.respondText("Wrong Username or password", status = HttpStatusCode.NotFound)
            } else {
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", loginRequest.username)
                    .withExpiresAt(Date(System.currentTimeMillis() + 600000))
                    .sign(Algorithm.HMAC256(secret))
                call.respond(hashMapOf("token" to token))
            }
        }

        authenticate("auth-jwt") {
            get("/secret-resources") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim("username").asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}

fun Application.registerAuthRoutes(environment: ApplicationEnvironment) {
    routing {
        authRoutes(environment)
    }
}

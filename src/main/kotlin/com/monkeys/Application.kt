package com.monkeys

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.monkeys.api.api
import com.monkeys.auth.auth
import com.monkeys.controller.AuthController
import com.monkeys.controller.UserController
import com.monkeys.models.AuthModel
import io.ktor.server.netty.*
import com.monkeys.repo.AuthRepo
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.ContentDisposition.Companion.File
import io.ktor.network.tls.certificates.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.serialization.*
import org.slf4j.event.Level
import java.io.File
import java.sql.SQLException

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.configure() {

    install(ContentNegotiation) {
        json()
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            call.request.path().startsWith("/")
        }
    }

    install(StatusPages) {
        exception<IllegalAccessException> { cause ->
            call.respond(
                status = HttpStatusCode.Unauthorized,
                message = cause.message!!
            )
        }
        exception<IllegalArgumentException> { cause ->
            call.respond(
                status = HttpStatusCode.NotFound,
                message = cause.message!!
            )
        }
        exception<SQLException> { cause ->
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = cause.message!!
            )
        }
    }

    install(Authentication) {
        jwt("validate") {
            verifier(
                JWT
                    .require(Algorithm.HMAC256("MySecretAlgorithm"))
                    .withIssuer("yana")
                    .build()
            )
            validate { credential ->
                val login = credential.payload.getClaim("login").asString()
                val psw = credential.payload.getClaim("psw").asString()
                if (login != null && psw != null) {
                    AuthModel(login, psw)
                } else {
                    null
                }
            }
        }
    }

    flywayMigrations()
    val authRepo = AuthRepo()
    val userController = UserController()

    routing {
        auth(AuthController(authRepo))
        api(userController)
    }
}

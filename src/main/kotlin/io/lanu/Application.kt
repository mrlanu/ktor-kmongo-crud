package io.lanu

import com.fasterxml.jackson.databind.SerializationFeature
import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import io.lanu.dao.CustomersDaoImpl
import io.lanu.dao.ICustomersDao
import io.lanu.routes.registerCustomerRoutes
import io.lanu.services.ICustomersService
import io.lanu.services.CustomersServiceImpl
import org.koin.core.context.startKoin
import org.koin.dsl.module
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun main(args: Array<String>) =
    io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {

    install(ContentNegotiation){
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    registerCustomerRoutes()

    startKoin {
        modules(module {
            single { getMongoClient(environment) }
            single<ICustomersDao> { CustomersDaoImpl(get()) }
            single<ICustomersService> { CustomersServiceImpl(get()) }
        })
    }
}

private fun getMongoClient(environment: ApplicationEnvironment): CoroutineClient {
    val username = environment.config.property("ktor.mongo.username").getString()
    val pass = environment.config.property("ktor.mongo.password").getString()
    val db = environment.config.property("ktor.mongo.db").getString()
    val credential = MongoCredential.createCredential(username, db, pass.toCharArray())
    val settings = MongoClientSettings.builder().credential(credential).build()
    return KMongo.createClient(settings).coroutine
}

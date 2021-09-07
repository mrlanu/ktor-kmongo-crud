package io.lanu.routes

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.lanu.models.Customer
import io.lanu.models.CustomerRequest
import io.lanu.services.ICustomersService
import org.koin.ktor.ext.inject

fun Route.customersRouting() {

    val service: ICustomersService by inject()

    route("/customers") {
        get {
            val users = service.findAll()
            if (users.isNotEmpty()){
                call.respond(users)
            } else {
                call.respondText("No customers have been found", status = HttpStatusCode.NotFound)
            }
        }
        authenticate("auth-jwt"){
            get("{id}") {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "Missed id",
                    status = HttpStatusCode.BadRequest
                )
                val customer = service.findOneById(id) ?: return@get call.respondText(
                    "No customer with id $id",
                    status = HttpStatusCode.NotFound
                )
                call.respond(customer)
            }
        }
        post {
            val request = call.receive<CustomerRequest>()
            val customer = Customer(firstName = request.firstName, lastName = request.lastName)
            service.saveCustomer(customer)
            call.respondText("Customer has been saved", status = HttpStatusCode.Created)
        }
        delete("{id}") {
            val id = call.parameters["id"] ?: return@delete call.respond(HttpStatusCode.BadRequest)
            val customer = service.findOneById(id) ?: return@delete call.respondText(
                "No customer with id $id",
                status = HttpStatusCode.NotFound
            )
            service.deleteById(id)
            if (service.findOneById(id) == null)
            {
                call.respondText("Customer has been removed", status = HttpStatusCode.Accepted)
            } else {
                call.respondText("Error", status = HttpStatusCode.NotFound)
            }
        }
    }
}

fun Application.registerCustomerRoutes() {
    routing {
        customersRouting()
    }
}

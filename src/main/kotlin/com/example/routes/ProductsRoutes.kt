package com.example.routes

import com.example.models.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.productsRouting() {
    get("/products/{categoryId?}") {
        val categoryId = call.parameters["categoryId"] ?: return@get call.respondText("Category ID parameter missing", status = HttpStatusCode.BadRequest)
        val products = productsStorage.filter { it.categoryId == categoryId.toInt() }
        call.respond(products)
    }
}
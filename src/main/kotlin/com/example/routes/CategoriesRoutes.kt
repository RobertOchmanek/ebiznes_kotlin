package com.example.routes

import com.example.models.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.categoriesRouting() {
    route("/categories") {
        get {
            call.respond(categoriesStorage)
        }
    }
}
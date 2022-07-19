package com.example.routes

import com.example.models.MessageRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.messengerRouting() {
    route("/webhook") {
        post {
            val messageRequest = call.receive<MessageRequest>()

            if (messageRequest.`object` == "page") {
                messageRequest.entry.forEach {
                    println(it.messaging[0].message)
                }
                call.respondText("EVENT_RECEIVED", status = HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get {
            val verifyToken = "<YOUR_VERIFY_TOKEN>"

            val mode = call.request.queryParameters["hub.mode"]
            val token = call.request.queryParameters["hub.verify_token"]
            val challenge = call.request.queryParameters["hub.challenge"]

            if (null != mode && null != token) {
                if (mode == "subscribe" && token == verifyToken) {
                    println("WEBHOOK_VERIFIED")
                    call.respondText(challenge!!, status = HttpStatusCode.OK)
                }
            } else {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}
package com.example.routes

import com.example.models.MessageRequest
import com.example.models.Messaging
import com.example.models.Recipient
import com.example.models.Response
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

const val verifyToken = ""
const val pageAccessToken = ""

fun Route.messengerRouting() {
    route("/webhook") {
        post {
            val messageRequest = call.receive<MessageRequest>()
            if (messageRequest.`object` == "page") {
                for (entry in messageRequest.entry) {
                    handleMessage(entry.messaging[0])
                }
                call.respondText("EVENT_RECEIVED", status = HttpStatusCode.OK)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        get {

            val mode = call.request.queryParameters["hub.mode"]
            val token = call.request.queryParameters["hub.verify_token"]
            val challenge = call.request.queryParameters["hub.challenge"]

            if (null != mode && null != token) {
                if (mode == "subscribe" && token == verifyToken) {
                    call.respondText(challenge!!, status = HttpStatusCode.OK)
                }
            } else {
                call.respond(HttpStatusCode.Forbidden)
            }
        }
    }
}

suspend fun handleMessage(messaging: Messaging) {
    val senderPsid = messaging.sender.id
    val text = messaging.message.text
    val message = "You sent the message: ${text}."

    callSendApi(senderPsid, message)
}

fun handlePostback() {

}

suspend fun callSendApi(senderPsid: String, message: String) {

    //TODO: serialize object to JSON instead of creating string response body
    val response = Response(Recipient(senderPsid), message)
    val responseJson = """{"recipient":{"id":${senderPsid}},"message":{"text":"$message"}}"""
    val httpClient = HttpClient()

    val httpResponse: HttpResponse = httpClient.request("https://graph.facebook.com/v14.0/me/messages") {
        method = HttpMethod.Post
        url {
            parameters.append("access_token", pageAccessToken)
        }
        contentType(ContentType.Application.Json)
        setBody(responseJson)
    }

    httpClient.close()
}
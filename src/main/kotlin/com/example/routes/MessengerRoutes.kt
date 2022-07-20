package com.example.routes

import com.example.models.*
import io.ktor.client.*
import io.ktor.client.request.*
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
    val responseMessage = getResponseMessage(text)

    callSendApi(senderPsid, responseMessage)
}

//TODO: messages formatting and real prices
//TODO: consider template & postback
fun getResponseMessage(text: String): String {
    if (text == "/Categories") {

        var categoriesResponse = "Currently available categories: "
        categoriesStorage.forEach {
            categoriesResponse += (it.name + " ")
        }
        return categoriesResponse

    } else if (text.startsWith("/")) {

        val matchingCategory = categoriesStorage.find { it.name == text.substring(1) }

        return if (null != matchingCategory) {

            var productsResponse = "Currently available products in ${matchingCategory.name} category: "
            productsStorage.forEach {
                if (it.categoryId == matchingCategory.id) {
                    productsResponse += (it.name + ", price: " + it.price + " ")
                }
            }
            productsResponse

        } else {
            "Sorry, such command does not exist."
        }
    } else {
        return "Hi! I am Ebiznes Kotlin Chatbot. You can request available categories by typing /Categories command or products in given category by typing /<category name>."
    }
}

suspend fun callSendApi(senderPsid: String, responseMessage: String) {

    //TODO: serialize object to JSON instead of creating string response body
    val response = Response(Recipient(senderPsid), responseMessage)
    val responseJson = """{"recipient":{"id":${senderPsid}},"message":{"text":"$responseMessage"}}"""
    val httpClient = HttpClient()

    httpClient.request("https://graph.facebook.com/v14.0/me/messages") {
        method = HttpMethod.Post
        url {
            parameters.append("access_token", pageAccessToken)
        }
        contentType(ContentType.Application.Json)
        setBody(responseJson)
    }

    httpClient.close()
}
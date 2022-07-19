package com.example.models

import kotlinx.serialization.Serializable

@Serializable
//TODO: name
data class MessageRequest(val objecty: String, val entry: List<Messaging>)
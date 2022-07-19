package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(val `object`: String, val entry: List<Messaging>)
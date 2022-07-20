package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Response(val recipient: Recipient, val message: String)
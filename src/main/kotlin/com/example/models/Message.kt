package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Message(val mid: String, val text: String)
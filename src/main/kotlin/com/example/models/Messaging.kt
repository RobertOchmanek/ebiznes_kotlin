package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Messaging(val messaging: List<Message>)
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Messaging(val sender: Sender, val recipient: Recipient, val timestamp: Long, val message: Message)
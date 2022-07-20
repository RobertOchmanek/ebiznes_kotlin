package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Entry(val id: String, val time: Long, val messaging: List<Messaging>)
package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(val id: Int, val name: String)

val categoriesStorage = listOf(
    Category(1, "Smartphone"),
    Category(2, "Tablet"),
    Category(3, "Notebook")
)
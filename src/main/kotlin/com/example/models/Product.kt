package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Product(val categoryId: Int, val name: String, val price: Double)

val productsStorage = listOf(
    Product(1, "iPhone 13 Pro Max", 1.0),
    Product(1, "iPhone 13", 2.0),
    Product(1, "iPhone SE", 3.0),
    Product(2, "iPad Pro", 4.0),
    Product(2, "iPad Air", 5.0),
    Product(2, "iPad Mini", 6.0),
    Product(3, "MacBook Pro", 7.0),
    Product(3, "MacBook Air", 8.0)
)
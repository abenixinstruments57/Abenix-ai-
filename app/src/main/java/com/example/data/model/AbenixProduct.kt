package com.example.data.Model

data class AbenixProduct(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: String,
    val currency: String = "USD",
    val available: Boolean = true
)

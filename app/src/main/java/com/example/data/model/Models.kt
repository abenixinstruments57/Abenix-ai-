package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sender: String, // "user" or "assistant"
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quotation_inquiries")
data class QuotationInquiry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val productName: String,
    val quantity: Int,
    val destinationCountry: String,
    val material: String = "Stainless Steel 410 / 420",
    val finish: String = "Satin / Matte Finish",
    val customization: String = "None",
    val specialSpecs: String = "",
    val status: String = "Inquiry Drafted",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "custom_spec_inquiries")
data class CustomSpecInquiry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val instrumentType: String,
    val photoRef: String = "",
    val dimensions: String = "",
    val materialRequirements: String = "",
    val logoBranding: String = "",
    val specialInstructions: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "bookmarked_products")
data class BookmarkedProduct(
    @PrimaryKey val id: String,
    val name: String,
    val category: String,
    val description: String,
    val material: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class InstrumentCatalogItem(
    val id: String,
    val name: String,
    val category: String, // Surgical, Dental, Orthopedic, Arthroscopy, Vascular, TC, Orthodontic, Surgical Sets, Custom
    val shortDescription: String,
    val fullSpecs: String,
    val recommendedMaterial: String,
    val features: List<String>,
    val imageResName: String = "img_custom_instruments"
)

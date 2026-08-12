package com.example.data.repository

import android.content.Context
import com.example.data.AbenixSettings
import com.example.data.Local.AbenixProductStore
import com.example.data.local.AbenixDao
import com.example.data.model.BookmarkedProduct
import com.example.data.model.ChatMessage
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.InstrumentCatalogItem
import com.example.data.model.QuotationInquiry
import com.example.data.Model.AbenixProduct
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.callAbenixAssistant
import kotlinx.coroutines.flow.Flow

class AbenixRepository(
    private val dao: AbenixDao,
    private val context: Context
) {

    private val settings = AbenixSettings(context)

    private val productStore = AbenixProductStore(context)

    val allMessages: Flow<List<ChatMessage>> =
        dao.getAllMessages()

    val allQuotations: Flow<List<QuotationInquiry>> =
        dao.getAllQuotations()

    val allCustomSpecs: Flow<List<CustomSpecInquiry>> =
        dao.getAllCustomSpecs()

    val bookmarkedProducts: Flow<List<BookmarkedProduct>> =
        dao.getBookmarkedProducts()


    // ============================================================
    // AI ASSISTANT
    // ============================================================

    suspend fun saveUserAndAssistantMessage(
        userText: String,
        history: List<ChatMessage>
    ): String {

        // Save customer message
        dao.insertMessage(
            ChatMessage(
                sender = "user",
                text = userText
            )
        )


        // --------------------------------------------------------
        // Convert conversation history for Gemini
        // --------------------------------------------------------

        val geminiHistory =
            history
                .takeLast(10)
                .map { message ->

                    GeminiContent(
                        role = if (message.sender == "user") {
                            "user"
                        } else {
                            "model"
                        },
                        parts = listOf(
                            GeminiPart(message.text)
                        )
                    )
                }


        // --------------------------------------------------------
        // Build company information from Admin Panel
        // --------------------------------------------------------

        val companyInfo = """
Company Name: ${settings.companyName}

Email 1: ${settings.email1}

Email 2: ${settings.email2}

WhatsApp / Phone: ${settings.phone}

Instagram: ${settings.instagram}
        """.trimIndent()


        // --------------------------------------------------------
        // Get Admin AI instructions
        // --------------------------------------------------------

        val adminInstructions =
            settings.aiInstructions


        // --------------------------------------------------------
        // Get products added through Admin Panel
        // --------------------------------------------------------

        val products =
            productStore.getProducts()


        // Convert products into text that Gemini can understand
        val productCatalog =
            buildProductCatalog(products)


        // --------------------------------------------------------
        // Call AI
        // --------------------------------------------------------

        val replyText =
            callAbenixAssistant(
                userPrompt = userText,
                history = geminiHistory,
                adminInstructions = adminInstructions,
                companyInfo = companyInfo,
                productCatalog = productCatalog
            )


        // --------------------------------------------------------
        // Save AI response
        // --------------------------------------------------------

        dao.insertMessage(
            ChatMessage(
                sender = "assistant",
                text = replyText
            )
        )

        return replyText
    }


    // ============================================================
    // PRODUCT CATALOG FOR AI
    // ============================================================

    private fun buildProductCatalog(
        products: List<AbenixProduct>
    ): String {

        if (products.isEmpty()) {
            return "No products have been added through the Admin Panel yet."
        }

        return products.joinToString(
            separator = "\n\n"
        ) { product ->

            """
PRODUCT ID: ${product.id}
PRODUCT NAME: ${product.name}
CATEGORY: ${product.category}
DESCRIPTION: ${product.description}
PRICE: ${
                if (product.price.isBlank()) {
                    "Not provided"
                } else {
                    "${product.price} ${product.currency}"
                }
            }
AVAILABILITY: ${
                if (product.available) {
                    "Available"
                } else {
                    "Unavailable"
                }
}
            """.trimIndent()
        }
    }


    // ============================================================
    // QUOTATIONS
    // ============================================================

    suspend fun saveQuotation(
        inquiry: QuotationInquiry
    ): Long {

        return dao.insertQuotation(inquiry)
    }

    suspend fun deleteQuotation(
        id: Long
    ) {

        dao.deleteQuotation(id)
    }


    // ============================================================
    // CUSTOM SPECIFICATIONS
    // ============================================================

    suspend fun saveCustomSpec(
        spec: CustomSpecInquiry
    ): Long {

        return dao.insertCustomSpec(spec)
    }

    suspend fun deleteCustomSpec(
        id: Long
    ) {

        dao.deleteCustomSpec(id)
    }


    // ============================================================
    // BOOKMARKS
    // ============================================================

    suspend fun toggleBookmark(
        item: InstrumentCatalogItem
    ) {

        if (dao.isBookmarked(item.id)) {

            dao.deleteBookmark(item.id)

        } else {

            dao.insertBookmark(
                BookmarkedProduct(
                    id = item.id,
                    name = item.name,
                    category = item.category,
                    description = item.shortDescription,
                    material = item.recommendedMaterial
                )
            )
        }
    }

    suspend fun isBookmarked(
        id: String
    ): Boolean {

        return dao.isBookmarked(id)
    }


    // ============================================================
    // BUILT-IN CATALOG
    // ============================================================

    fun getCatalogItems(): List<InstrumentCatalogItem> {

        return listOf(

            InstrumentCatalogItem(
                id = "SURG-01",
                name = "Mayo Surgical Scissors (Straight/Curved)",
                category = "Surgical",
                shortDescription = "Precision stainless steel surgical scissors for heavy tissue cutting and suture dissection.",
                fullSpecs = "Made from Japanese Stainless Steel AISI

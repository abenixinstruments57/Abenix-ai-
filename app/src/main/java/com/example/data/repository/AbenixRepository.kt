package com.example.data.repository

import com.example.data.AbenixSettings
import com.example.data.Local.AbenixProductStore
import com.example.data.local.AbenixDao
import com.example.data.model.BookmarkedProduct
import com.example.data.model.ChatMessage
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.InstrumentCatalogItem
import com.example.data.model.QuotationInquiry
import com.example.data.remote.GeminiContent
import com.example.data.remote.GeminiPart
import com.example.data.remote.callAbenixAssistant
import kotlinx.coroutines.flow.Flow

class AbenixRepository(private val dao: AbenixDao) {

    val allMessages: Flow<List<ChatMessage>> =
        dao.getAllMessages()

    val allQuotations: Flow<List<QuotationInquiry>> =
        dao.getAllQuotations()

    val allCustomSpecs: Flow<List<CustomSpecInquiry>> =
        dao.getAllCustomSpecs()

    val bookmarkedProducts: Flow<List<BookmarkedProduct>> =
        dao.getBookmarkedProducts()

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

        /*
         * Convert recent conversation history for Gemini.
         *
         * Keeping the recent conversation allows the AI to understand
         * follow-up questions such as:
         *
         * User: Tell me about Kerrison Rongeur
         * User: What is the price?
         *
         * The second question can now be understood in context.
         */
        val geminiHistory =
            history
                .takeLast(10)
                .map { message ->

                    GeminiContent(
                        role =
                            if (message.sender == "user")
                                "user"
                            else
                                "model",

                        parts = listOf(
                            GeminiPart(message.text)
                        )
                    )
                }

        /*
         * Load Admin Panel settings.
         */
        val context = dao.getApplicationContext()

        val settings = AbenixSettings(context)

        /*
         * Load products created/edited from the Admin Panel.
         */
        val productStore = AbenixProductStore(context)

        val products = productStore.getProducts()

        /*
         * Convert products into AI-readable text.
         */
        val productCatalog =
            if (products.isEmpty()) {

                "No custom products have been added through the Admin Panel."

            } else {

                products.joinToString("\n\n") { product ->

                    """
PRODUCT
Name: ${product.name}
Category: ${product.category}
Description: ${product.description}
Price: ${
                        if (product.price.isBlank())
                            "Not provided"
                        else
                            product.price
                    }
Currency: ${product.currency}
Availability: ${
                        if (product.available)
                            "Available"
                        else
                            "Unavailable"
                    }
                    """.trimIndent()
                }
            }

        /*
         * Company information available to the AI.
         */
        val companyInfo = """
Company Name: ${settings.companyName}
Email 1: ${settings.email1}
Email 2: ${settings.email2}
WhatsApp / Phone: ${settings.phone}
Instagram: ${settings.instagram}
        """.trimIndent()

        /*
         * Admin-controlled AI instructions.
         */
        val adminInstructions =
            settings.aiInstructions

        /*
         * Send everything to the improved Abenix AI.
         */
        val replyText =
            callAbenixAssistant(
                userPrompt = userText,
                history = geminiHistory,
                adminInstructions = adminInstructions,
                companyInfo = companyInfo,
                productCatalog = productCatalog
            )

        // Save AI response
        dao.insertMessage(
            ChatMessage(
                sender = "assistant",
                text = replyText
            )
        )

        return replyText
    }

    suspend fun saveQuotation(
        inquiry: QuotationInquiry
    ): Long =
        dao.insertQuotation(inquiry)

    suspend fun deleteQuotation(
        id: Long
    ) =
        dao.deleteQuotation(id)

    suspend fun saveCustomSpec(
        spec: CustomSpecInquiry
    ): Long =
        dao.insertCustomSpec(spec)

    suspend fun deleteCustomSpec(
        id: Long
    ) =
        dao.deleteCustomSpec(id)

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
    ): Boolean =
        dao.isBookmarked(id)

    /*
     * Built-in Abenix catalog.
     *
     * This remains available for the existing Catalog screen.
     */
    fun getCatalogItems(): List<InstrumentCatalogItem> {

        return listOf(

            InstrumentCatalogItem(
                id = "SURG-01",
                name = "Mayo Surgical Scissors (Straight/Curved)",
                category = "Surgical",
                shortDescription =
                    "Precision stainless steel surgical scissors for heavy tissue cutting and suture dissection.",
                fullSpecs =
                    "Made from Japanese Stainless Steel AISI 420. Fully autoclave safe, rust-resistant, passivated finish.",
                recommendedMaterial =
                    "AISI 420 Stainless Steel",
                features =
                    listOf(
                        "Beveled precision blades",
                        "Satin anti-glare finish",
                        "Available in 14cm - 23cm sizes"
                    ),
                imageResName =
                    "img_abenix_hero_banner"
            ),

            InstrumentCatalogItem(
                id = "SURG-02",
                name = "Adson Tissue Forceps (1x2 Teeth)",
                category = "Surgical",
                shortDescription =
                    "Delicate thumb forceps designed for gripping aggressive or delicate tissue safely.",
                fullSpecs =
                    "Micro-fine interlocking teeth for secure grip without slipping. Ergonomic wide handle grip.",
                recommendedMaterial =
                    "AISI 304 / 420 Surgical Grade",
                features =
                    listOf(
                        "1x2 interlocking teeth",
                        "Non-slip thumb grip",
                        "Length: 12cm / 15cm"
                    ),
                imageResName =
                    "img_custom_instruments"
            ),

            InstrumentCatalogItem(
                id = "TC-01",
                name = "Crile-Wood TC Needle Holder",
                category = "TC",
                shortDescription =
                    "Tungsten Carbide tipped needle holder with gold rings for extreme grip and durability.",
                fullSpecs =
                    "T

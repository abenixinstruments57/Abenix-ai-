package com.example.data.repository

import android.content.Context
import com.example.data.AbenixSettings
import com.example.data.Local.AbenixProductStore
import com.example.data.Model.AbenixProduct
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

        dao.insertMessage(
            ChatMessage(
                sender = "user",
                text = userText
            )
        )

        val geminiHistory = history
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

        val companyInfo = """
Company Name: ${settings.companyName}
Email 1: ${settings.email1}
Email 2: ${settings.email2}
WhatsApp / Phone: ${settings.phone}
Instagram: ${settings.instagram}
        """.trimIndent()

        val adminInstructions = settings.aiInstructions

        val products = productStore.getProducts()

        val productCatalog = buildProductCatalog(products)

        val replyText = callAbenixAssistant(
            userPrompt = userText,
            history = geminiHistory,
            adminInstructions = adminInstructions,
            companyInfo = companyInfo,
            productCatalog = productCatalog
        )

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

        return products.joinToString("\n\n") { product ->

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
                name = "Mayo Surgical Scissors",
                category = "Surgical",
                shortDescription = "Surgical scissors for cutting and dissection.",
                fullSpecs = "Professional surgical scissors.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision manufactured",
                    "Reusable",
                    "Surgical application"
                )
            ),

            InstrumentCatalogItem(
                id = "SURG-02",
                name = "Adson Tissue Forceps",
                category = "Surgical",
                shortDescription = "Precision forceps for handling tissue.",
                fullSpecs = "Professional surgical tissue forceps.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision tips",
                    "Reusable",
                    "Surgical application"
                )
            ),

            InstrumentCatalogItem(
                id = "SURG-03",
                name = "Mayo Hegar Needle Holder",
                category = "Surgical",
                shortDescription = "Needle holder for surgical suturing procedures.",
                fullSpecs = "Professional surgical needle holder.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Secure needle grip",
                    "Reusable",
                    "Surgical application"
                )
            ),

            InstrumentCatalogItem(
                id = "DENT-01",
                name = "Dental Extraction Forceps",
                category = "Dental",
                shortDescription = "Dental forceps for tooth extraction procedures.",
                fullSpecs = "Professional dental extraction forceps.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision manufactured",
                    "Reusable",
                    "Dental application"
                )
            ),

            InstrumentCatalogItem(
                id = "DENT-02",
                name = "Dental Explorer",
                category = "Dental",
                shortDescription = "Dental diagnostic instrument for examination.",
                fullSpecs = "Professional dental explorer.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Fine working tip",
                    "Reusable",
                    "Dental application"
                )
            ),

            InstrumentCatalogItem(
                id = "ORTH-01",
                name = "Orthopedic Chisel",
                category = "Orthopedic",
                shortDescription = "Orthopedic instrument for bone procedures.",
                fullSpecs = "Professional orthopedic chisel.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Durable construction",
                    "Reusable",
                    "Orthopedic application"
                )
            ),

            InstrumentCatalogItem(
                id = "ARTH-01",
                name = "Arthroscopy Grasper",
                category = "Arthroscopy",
                shortDescription = "Arthroscopic instrument for minimally invasive procedures.",
                fullSpecs = "Professional arthroscopy grasper.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision mechanism",
                    "Reusable",
                    "Arthroscopy application"
                )
            ),

            InstrumentCatalogItem(
                id = "VASC-01",
                name = "Vascular Clamp",
                category = "Vascular",
                shortDescription = "Clamp designed for vascular surgical procedures.",
                fullSpecs = "Professional vascular surgical clamp.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision control",
                    "Reusable",
                    "Vascular application"
                )
            ),

            InstrumentCatalogItem(
                id = "TC-01",
                name = "TC Surgical Scissors",
                category = "TC",
                shortDescription = "Tungsten Carbide surgical scissors.",
                fullSpecs = "Professional TC surgical scissors.",
                recommendedMaterial = "Stainless Steel with Tungsten Carbide",
                features = listOf(
                    "Tungsten Carbide inserts",
                    "Precision cutting",
                    "Reusable"
                )
            ),

            InstrumentCatalogItem(
                id = "ORTHO-01",
                name = "Orthodontic Plier",
                category = "Orthodontic",
                shortDescription = "Orthodontic plier for dental orthodontic procedures.",
                fullSpecs = "Professional orthodontic plier.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Precision tips",
                    "Reusable",
                    "Orthodontic application"
                )
            ),

            InstrumentCatalogItem(
                id = "SET-01",
                name = "Surgical Instrument Set",
                category = "Surgical Sets",
                shortDescription = "Customizable surgical instrument set.",
                fullSpecs = "Instrument set composition can be customized according to customer requirements.",
                recommendedMaterial = "Stainless Steel",
                features = listOf(
                    "Customizable composition",
                    "Professional instruments",
                    "OEM options"
                )
            ),

            InstrumentCatalogItem(
                id = "CUSTOM-01",
                name = "Custom OEM Instrument",
                category = "Custom",
                shortDescription = "Customized surgical and medical instruments according to customer requirements.",
                fullSpecs = "Custom manufacturing available according to customer drawings, samples, dimensions and specifications.",
                recommendedMaterial = "As specified by customer",
                features = listOf(
                    "OEM manufacturing",
                    "Custom dimensions",
                    "Custom branding"
                )
            )
        )
    }
}

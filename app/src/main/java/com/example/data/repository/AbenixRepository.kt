package com.example.data.repository

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

    val allMessages: Flow<List<ChatMessage>> = dao.getAllMessages()
    val allQuotations: Flow<List<QuotationInquiry>> = dao.getAllQuotations()
    val allCustomSpecs: Flow<List<CustomSpecInquiry>> = dao.getAllCustomSpecs()
    val bookmarkedProducts: Flow<List<BookmarkedProduct>> = dao.getBookmarkedProducts()

    suspend fun saveUserAndAssistantMessage(userText: String, history: List<ChatMessage>): String {
        // Save user message
        dao.insertMessage(ChatMessage(sender = "user", text = userText))

        // Convert history for Gemini
        val geminiHistory = history.takeLast(6).map { msg ->
            GeminiContent(
                role = if (msg.sender == "user") "user" else "model",
                parts = listOf(GeminiPart(msg.text))
            )
        }

        // Call Gemini or fallback
        val replyText = callAbenixAssistant(userText, geminiHistory)

        // Save assistant reply
        dao.insertMessage(ChatMessage(sender = "assistant", text = replyText))
        return replyText
    }

    suspend fun saveQuotation(inquiry: QuotationInquiry): Long = dao.insertQuotation(inquiry)
    suspend fun deleteQuotation(id: Long) = dao.deleteQuotation(id)

    suspend fun saveCustomSpec(spec: CustomSpecInquiry): Long = dao.insertCustomSpec(spec)
    suspend fun deleteCustomSpec(id: Long) = dao.deleteCustomSpec(id)

    suspend fun toggleBookmark(item: InstrumentCatalogItem) {
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

    suspend fun isBookmarked(id: String): Boolean = dao.isBookmarked(id)

    // Verified catalog data for Abenix Instruments
    fun getCatalogItems(): List<InstrumentCatalogItem> {
        return listOf(
            InstrumentCatalogItem(
                id = "SURG-01",
                name = "Mayo Surgical Scissors (Straight/Curved)",
                category = "Surgical",
                shortDescription = "Precision stainless steel surgical scissors for heavy tissue cutting and suture dissection.",
                fullSpecs = "Made from Japanese Stainless Steel AISI 420. Fully autoclave safe, rust-resistant, passivated finish.",
                recommendedMaterial = "AISI 420 Stainless Steel",
                features = listOf("Beveled precision blades", "Satin anti-glare finish", "Available in 14cm - 23cm sizes"),
                imageResName = "img_abenix_hero_banner"
            ),
            InstrumentCatalogItem(
                id = "SURG-02",
                name = "Adson Tissue Forceps (1x2 Teeth)",
                category = "Surgical",
                shortDescription = "Delicate thumb forceps designed for gripping aggressive or delicate tissue safely.",
                fullSpecs = "Micro-fine interlocking teeth for secure grip without slipping. Ergonomic wide handle grip.",
                recommendedMaterial = "AISI 304 / 420 Surgical Grade",
                features = listOf("1x2 interlocking teeth", "Non-slip thumb grip", "Length: 12cm / 15cm"),
                imageResName = "img_custom_instruments"
            ),
            InstrumentCatalogItem(
                id = "TC-01",
                name = "Crile-Wood TC Needle Holder",
                category = "TC",
                shortDescription = "Tungsten Carbide tipped needle holder with gold rings for extreme grip and durability.",
                fullSpecs = "Tungsten Carbide serrated inserts prevent needle slipping during fine delicate suturing.",
                recommendedMaterial = "German Tungsten Carbide Inserts",
                features = listOf("Gold plated ring handles", "Serrated TC jaws", "Sizes: 15cm - 20cm"),
                imageResName = "img_custom_instruments"
            ),
            InstrumentCatalogItem(
                id = "DENT-01",
                name = "Dental Extraction Forceps Set",
                category = "Dental",
                shortDescription = "Anatomically contoured extraction forceps for upper and lower anterior & molar teeth.",
                fullSpecs = "Corrosion-resistant medical grade steel with knurled non-slip handle profile.",
                recommendedMaterial = "Stainless Steel 410 / Satin Finish",
                features = listOf("Anatomic beak contours", "Knurled handles", "Set of 10 standard forceps"),
                imageResName = "img_custom_instruments"
            ),
            InstrumentCatalogItem(
                id = "DENT-02",
                name = "Periodontal Explorer & Scaler Set",
                category = "Dental",
                shortDescription = "Double-ended dental explorers and sickle scalers for precise calculus removal.",
                fullSpecs = "Hollow lightweight handle reduces hand fatigue during long clinical procedures.",
                recommendedMaterial = "Stainless Steel 304 Handle + Hardened Tips",
                features = listOf("Hollow 8mm handle", "Sharp durable tips", "Color-coded rings available"),
                imageResName = "img_custom_instruments"
            ),
            InstrumentCatalogItem(
                id = "ORTHO-01",
                name = "Liston Bone Cutting Forceps",
                category = "Orthopedic",
                shortDescription = "Heavy duty double-action bone cutting forceps for orthopedics and trauma surgery.",
                fullSpecs = "Double action compound joint delivers superior leverage with minimal hand pressure.",
                recommendedMaterial = "High-Tensile German Grade Stainless Steel",
                features = listOf("Double action leverage", "Straight/Angled jaw choices", "Length: 19cm - 28cm"),
                imageResName = "img_custom_instruments"
            ),
            InstrumentCatalogItem(
                id = "VASC-01",
                name = "DeBakey Vascular Clamps",
                category = "Vascular",
                shortDescription = "Atraumatic vascular clamps designed to occlude blood vessels without tissue trauma.",
                fullSpecs = "Special atraumatic DeBakey ribbing along jaw inner surfaces.",
                recommendedMaterial = "Non-Magnetic Surgical Stainless Steel",
                features = listOf("Atraumatic jaw teeth", "Ratchet locking mechanism", "Various angle bends"),
                imageResName = "img_abenix_hero_banner"
            ),
            InstrumentCatalogItem(
                id = "SET-01",
                name = "Major General Surgery Instrument Set",
                category = "Surgical Sets",
                shortDescription = "Complete 62-piece surgical set for operating rooms and hospital surgical departments.",
                fullSpecs = "Includes scalpels, towel clamps, retractors, scissors, forceps, and needle holders in sterilization box.",
                recommendedMaterial = "Full Surgical Grade Stainless Steel",
                features = listOf("62 essential instruments", "Sterilization container included", "Custom set packing options"),
                imageResName = "img_abenix_hero_banner"
            ),
            InstrumentCatalogItem(
                id = "OEM-01",
                name = "Customized OEM Surgical Instruments",
                category = "Custom",
                shortDescription = "Tailor-made surgical tools fabricated according to customer technical drawings or samples.",
                fullSpecs = "Full OEM/ODM manufacturing capability in Sialkot, Pakistan. Custom laser branding and packaging.",
                recommendedMaterial = "Custom Material Specification (AISI 304, 420, TC, Titanium)",
                features = listOf("Custom laser engraving", "Prototype sample approval", "Bulk export box packaging"),
                imageResName = "img_custom_instruments"
            )
        )
    }
}

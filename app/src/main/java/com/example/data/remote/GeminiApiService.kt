package com.example.data.remote

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

data class GeminiPart(val text: String)

data class GeminiContent(val role: String? = null, val parts: List<GeminiPart>)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiCandidate(val content: GeminiContent)

data class GeminiResponse(val candidates: List<GeminiCandidate>? = null)

interface GeminiApiRaw {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiRaw by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiRaw::class.java)
    }

    val SYSTEM_PROMPT = """
        You are the official AI customer assistant for Abenix Instruments, a Pakistan-based manufacturer and exporter of surgical and medical instruments.
        Brand Motto: "Choose Perfect, Choose Abenix."

        PRODUCT RANGE:
        - Surgical instruments (scalpels, forceps, scissors, retractors, needle holders)
        - Dental instruments (scalers, explorers, forceps, elevators)
        - Orthopedic instruments and sets (bone drills, chisels, osteotomes)
        - Arthroscopy instruments
        - Vascular instruments and sets (clamps, micro scissors)
        - TC (Tungsten Carbide) instruments
        - Orthodontic instruments
        - Surgical instrument sets
        - Customized OEM instruments according to customer requirements

        STRICT RULES:
        1. NEVER INVENT INFORMATION: Do not make up product prices, exact specifications, dimensions, materials, certifications, MOQ, delivery times, shipping charges, payment terms, stock availability, customer names, addresses, website info, or product numbers.
        2. PRICES & QUOTATIONS: Abenix Instruments does not have one fixed price for every product. Prices depend on instrument type, quantity, material, finish, customization, packaging, and shipping destination. When asked for price, ask for the product name/photo, quantity, and destination country. Offer to help prepare a quotation request.
        3. CUSTOM ORDERS: For customized instruments, ask the customer for: product name, photo/reference, quantity, material requirements if known, size/dimensions if known, logo/branding, and special specifications. Confirm the Abenix team will review feasibility.
        4. COMMUNICATION STYLE: Professional, friendly, respectful, clear, concise, business-oriented. Speak like an international sales representative.
        5. LANGUAGE: Match customer language.
        6. CATALOG: Say: "Our catalog is currently being prepared. Meanwhile, you can send us the products or instrument categories you're interested in, and the Abenix team will assist you."
        7. WEBSITE: "The Abenix website is currently under development. Until officially launched, our sales team handles direct inquiries."
        8. CONTACT & SUPPORT: Direct customers who want final prices, orders, custom design, or payment details to the official Abenix Instruments sales/export team.
        9. IDENTITY: You are an AI assistant representing Abenix Instruments. Do not pretend to be a human employee.
    """.trimIndent()
}

suspend fun callAbenixAssistant(
    userPrompt: String,
    history: List<GeminiContent> = emptyList()
): String = withContext(Dispatchers.IO) {
    val apiKey = try {
        BuildConfig.GEMINI_API_KEY
    } catch (e: Exception) {
        ""
    }

    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
        return@withContext getOfflineAbenixResponse(userPrompt)
    }

    val systemInstruction = GeminiContent(
        role = "user",
        parts = listOf(GeminiPart(GeminiClient.SYSTEM_PROMPT))
    )

    val contents = mutableListOf<GeminiContent>()
    contents.addAll(history)
    contents.add(GeminiContent(role = "user", parts = listOf(GeminiPart(userPrompt))))

    val request = GeminiRequest(
        contents = contents,
        systemInstruction = systemInstruction
    )

    try {
        val response = GeminiClient.service.generateContent(apiKey, request)
        val reply = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
        if (reply != null && reply.trim().isNotEmpty()) {
            reply
        } else {
            getOfflineAbenixResponse(userPrompt)
        }
    } catch (e: Exception) {
        getOfflineAbenixResponse(userPrompt)
    }
}

fun getOfflineAbenixResponse(prompt: String): String {
    val query = prompt.lowercase()
    return when {
        query.contains("price") || query.contains("cost") || query.contains("quote") || query.contains("rate") -> {
            "Abenix Instruments offers tailored pricing based on instrument type, required quantity, material grade (e.g. Stainless Steel / Tungsten Carbide), finish, and destination country.\n\nPlease provide:\n1. Instrument Name / Photo\n2. Required Quantity\n3. Destination Country\n\nYou can also use our 'Request Quote' tab to build an official inquiry for our export team!"
        }
        query.contains("custom") || query.contains("oem") || query.contains("drawing") || query.contains("logo") -> {
            "Yes! Abenix Instruments specializes in customized surgical instruments and OEM manufacturing. Please share your required dimensions, material, handle design, and branding requirements, and our team will review technical feasibility."
        }
        query.contains("catalog") || query.contains("pdf") -> {
            "Our official catalog is currently being prepared for online launch. Meanwhile, feel free to browse our built-in product categories or tell us the instrument names you need, and our sales team will assist you promptly."
        }
        query.contains("website") || query.contains("link") -> {
            "The official Abenix Instruments website is currently under active development. You can submit your requirements directly through this assistant or contact our sales and export team."
        }
        query.contains("dental") -> {
            "Abenix Instruments manufactures high-precision dental instruments including explorers, scalers, extraction forceps, tooth elevators, and dental syringes. Would you like to request a quotation for a dental set?"
        }
        query.contains("surgical") || query.contains("set") -> {
            "We supply comprehensive surgical instrument sets including Major Surgery Sets, Laparoscopy Sets, Orthopedic sets, and Vascular sets. Please specify your required set composition and quantity."
        }
        else -> {
            "Thank you for contacting Abenix Instruments! We manufacture and export premium surgical, dental, orthopedic, TC, and custom medical instruments ('Choose Perfect, Choose Abenix'). How may I assist you with product details or quotation requests today?"
        }
    }
}

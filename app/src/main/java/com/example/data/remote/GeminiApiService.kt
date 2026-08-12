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

data class GeminiContent(
    val role: String? = null,
    val parts: List<GeminiPart>
)

data class GeminiRequest(
    val contents: List<GeminiContent>,
    val systemInstruction: GeminiContent? = null
)

data class GeminiCandidate(
    val content: GeminiContent
)

data class GeminiResponse(
    val candidates: List<GeminiCandidate>? = null
)

interface GeminiApiRaw {

    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

object GeminiClient {

    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/"

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
}

/*
 * Default Abenix AI instructions.
 *
 * These can later be combined with the instructions
 * saved from the Admin Panel.
 */
const val DEFAULT_ABENIX_SYSTEM_PROMPT = """
You are Abenix AI, the official AI customer assistant for Abenix Instruments.

Abenix Instruments is a Pakistan-based manufacturer and exporter of surgical and medical instruments.

Brand motto:
"Choose Perfect, Choose Abenix."

YOUR JOB:
Help customers understand Abenix products, instrument categories,
custom manufacturing, quotations, and general company information.

IMPORTANT BEHAVIOR:

1. ANSWER THE ACTUAL QUESTION
Do not repeatedly send the same generic welcome message.

If the customer asks about a specific instrument, answer specifically
about that instrument when information is available.

2. USE THE PROVIDED PRODUCT DATA
You will receive an Abenix product catalog below.

Use that information when answering questions.

If a product is not in the provided catalog, do not invent specifications,
prices, materials, sizes, availability, certifications, MOQ, delivery time,
or other technical information.

3. PRICES
Only provide a product price if an actual price is present in the
provided Abenix product data.

If no price is available, explain that pricing depends on the customer's
requirements and ask for the required quantity and destination country.

4. CUSTOM PRODUCTS
For customized or OEM instruments, ask for relevant information such as:

- Product name
- Reference photo or drawing
- Quantity
- Material
- Size/dimensions
- Logo or branding
- Special requirements

5. CONVERSATION
Remember the recent conversation and don't repeat information unnecessarily.

If the customer asks a follow-up question such as:
"What about the price?"
understand that they are referring to the product discussed immediately before.

6. LANGUAGE
Match the customer's language whenever reasonably possible.

7. PROFESSIONAL STYLE
Be professional, friendly, concise, and helpful.

Speak like a professional international sales assistant.

8. DO NOT PRETEND TO BE HUMAN
You are an AI assistant representing Abenix Instruments.

9. CONTACT INFORMATION
Use the official company contact information provided in the
company information section below.

10. NO INVENTION
Never invent company information, product information,
prices, certifications, customer information, addresses,
shipping charges, payment terms, or availability.

11. WEBSITE
Do not claim that an official website exists unless it is provided
in the company information.

12. CATALOG
If the customer asks for the catalog and no catalog link is provided,
explain that they can browse the available products in the app
or contact Abenix Instruments for the requested products.
"""

/**
 * Builds the complete system prompt using:
 *
 * 1. Default Abenix AI behavior
 * 2. Admin Panel instructions
 * 3. Company information
 * 4. Products entered through Admin Panel
 */
fun buildAbenixSystemPrompt(
    adminInstructions: String = "",
    companyInfo: String = "",
    productCatalog: String = ""
): String {

    return """
$DEFAULT_ABENIX_SYSTEM_PROMPT

==============================
ADMIN PANEL INSTRUCTIONS
==============================

${adminInstructions.ifBlank {
        "Follow the standard Abenix AI instructions."
    }}

==============================
OFFICIAL COMPANY INFORMATION
==============================

${companyInfo.ifBlank {
        "No additional company information has been provided."
    }}

==============================
ABENIX PRODUCT CATALOG
==============================

${productCatalog.ifBlank {
        "No custom products have been added yet."
    }}

==============================
FINAL INSTRUCTION
==============================

Use the information above as the source of truth.

Answer the customer's actual question.

Do NOT repeat a generic Abenix introduction unless it is appropriate
for the customer's first message.

If the customer asks about a specific product, focus on that product.

If information is unavailable, clearly say that it is not currently
available rather than inventing an answer.
""".trimIndent()
}

/**
 * Calls Gemini with:
 *
 * - User question
 * - Conversation history
 * - Admin instructions
 * - Company information
 * - Products entered through Admin Panel
 */
suspend fun callAbenixAssistant(
    userPrompt: String,
    history: List<GeminiContent> = emptyList(),
    adminInstructions: String = "",
    companyInfo: String = "",
    productCatalog: String = ""
): String = withContext(Dispatchers.IO) {

    val apiKey = try {
        BuildConfig.GEMINI_API_KEY
    } catch (e: Exception) {
        ""
    }

    /*
     * If Gemini API is not configured, use the improved
     * local/offline response system.
     */
    if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {

        return@withContext getOfflineAbenixResponse(
            prompt = userPrompt,
            productCatalog = productCatalog
        )
    }

    val systemPrompt = buildAbenixSystemPrompt(
        adminInstructions = adminInstructions,
        companyInfo = companyInfo,
        productCatalog = productCatalog
    )

    val systemInstruction = GeminiContent(
        role = "user",
        parts = listOf(
            GeminiPart(systemPrompt)
        )
    )

    val contents = mutableListOf<GeminiContent>()

    /*
     * Keep recent conversation history.
     */
    contents.addAll(history.takeLast(10))

    /*
     * Add the current customer question.
     */
    contents.add(
        GeminiContent(
            role = "user",
            parts = listOf(
                GeminiPart(userPrompt)
            )
        )
    )

    val request = GeminiRequest(
        contents = contents,
        systemInstruction = systemInstruction
    )

    try {

        val response =
            GeminiClient.service.generateContent(
                apiKey = apiKey,
                request = request
            )

        val reply =
            response
                .candidates
                ?.firstOrNull()
                ?.content
                ?.parts
                ?.firstOrNull()
                ?.text

        if (!reply.isNullOrBlank()) {

            reply.trim()

        } else {

            getOfflineAbenixResponse(
                prompt = userPrompt,
                productCatalog = productCatalog
            )
        }

    } catch (e: Exception) {

        getOfflineAbenixResponse(
            prompt = userPrompt,
            productCatalog = productCatalog
        )
    }
}

/**
 * Improved offline assistant.
 *
 * This is only used when Gemini isn't available.
 */
fun getOfflineAbenixResponse(
    prompt: String,
    productCatalog: String = ""
): String {

    val query = prompt.lowercase()

    /*
     * Try to find a product mentioned in the customer's question.
     */
    val catalogLines = productCatalog
        .split("\n")
        .filter { it.isNotBlank() }

    val matchingProduct =
        catalogLines.firstOrNull { line ->

            val words = query
                .split(" ")
                .filter { it.length >= 4 }

            words.any {
                line.lowercase().contains(it)
            }
        }

    if (matchingProduct != null) {

        return """
Here is the relevant Abenix Instruments information I found:

$matchingProduct

If you need pricing or a quotation, please provide the required quantity and destination country.
        """.trimIndent()
    }

    return when {

        query.contains("price") ||
        query.contains("cost") ||
        query.contains("quote") ||
        query.contains("rate") -> {

            """
Abenix Instruments provides quotation-based pricing.

Please provide:
1. Instrument name or photo
2. Required quantity
3. Destination country

Our team can then prepare the appropriate quotation.
            """.trimIndent()
        }

        query.contains("custom") ||
        query.contains("oem") ||
        query.contains("drawing") ||
        query.contains("logo") -> {

            """
Yes, Abenix Instruments supports customized and OEM surgical instruments.

Please send:
• Product name or reference photo
• Quantity
• Required material
• Size/dimensions
• Logo/branding requirements
• Any special specifications

Our team can review the requirements.
            """.trimIndent()
        }

        query.contains("catalog") ||
        query.contains("products") -> {

            if (productCatalog.isNotBlank()) {

                """
Here are some products currently available in the Abenix AI catalog:

$productCatalog
                """.trimIndent()

            } else {

                """
Our product catalog is currently being prepared.

You can tell me the instrument or category you are looking for,
and I can assist with your inquiry.
                """.trimIndent()
            }
        }

        query.contains("dental") -> {

            """
Abenix Instruments supplies dental instruments and sets.

You can tell me the specific dental instrument you need,
and I can help with the product information or quotation request.
            """.trimIndent()
        }

        query.contains("orthopedic") ||
        query.contains("orthopaedic") -> {

            """
Abenix Instruments supplies orthopedic instruments and sets.

Tell me the specific orthopedic instrument you are interested in,
and I can help with the available information.
            """.trimIndent()
        }

        query.contains("surgical") -> {

            """
Abenix Instruments manufactures and exports surgical instruments
and surgical sets.

Tell me the specific instrument or set you are looking for,
and I can help with the available information.
            """.trimIndent()
        }

        else -> {

            """
Thank you for contacting Abenix Instruments.

I can help you with surgical, dental, orthopedic, vascular,
TC, orthodontic, surgical sets, and customized OEM instruments.

What instrument or product would you like to know about?
            """.trimIndent()
        }
    }
}

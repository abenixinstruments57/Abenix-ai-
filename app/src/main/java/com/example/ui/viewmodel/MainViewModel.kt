package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.BookmarkedProduct
import com.example.data.model.ChatMessage
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.InstrumentCatalogItem
import com.example.data.model.QuotationInquiry
import com.example.data.repository.AbenixRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val repository: AbenixRepository

    val messages: StateFlow<List<ChatMessage>>
    val quotations: StateFlow<List<QuotationInquiry>>
    val customSpecs: StateFlow<List<CustomSpecInquiry>>
    val bookmarkedProducts: StateFlow<List<BookmarkedProduct>>

    init {

        val dao = AppDatabase
            .getDatabase(application)
            .abenixDao()

        repository = AbenixRepository(
            dao = dao,
            context = application.applicationContext
        )

        messages = repository.allMessages.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        quotations = repository.allQuotations.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        customSpecs = repository.allCustomSpecs.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        bookmarkedProducts = repository.bookmarkedProducts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {

            val existing = repository.allMessages.first()

            if (existing.isEmpty()) {

                dao.insertMessage(
                    ChatMessage(
                        sender = "assistant",
                        text = "Hello! 👋 Welcome to Abenix Instruments.\n\nI'm the Abenix AI Assistant, here to help you with surgical and medical instruments, product inquiries, customized requirements, quotations, and general company information.\n\nHow can I help you today?"
                    )
                )
            }
        }
    }

    private val _selectedTab = MutableStateFlow(0)
    val selectedTab: StateFlow<Int> = _selectedTab.asStateFlow()

    fun setSelectedTab(tab: Int) {
        _selectedTab.value = tab
    }

    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending

    fun updateChatInput(text: String) {
        _chatInput.value = text
    }

    fun sendMessage(userText: String? = null) {

        val query = (userText ?: _chatInput.value).trim()

        if (query.isEmpty() || _isSending.value) {
            return
        }

        _chatInput.value = ""
        _isSending.value = true

        viewModelScope.launch {

            try {

                val currentList = messages.value

                repository.saveUserAndAssistantMessage(
                    userText = query,
                    history = currentList
                )

            } catch (e: Exception) {

                e.printStackTrace()

            } finally {

                _isSending.value = false
            }
        }
    }

    val catalogItems: List<InstrumentCatalogItem> =
        repository.getCatalogItems()

    private val _selectedCategory =
        MutableStateFlow("All")

    val selectedCategory: StateFlow<String> =
        _selectedCategory.asStateFlow()

    private val _searchQuery =
        MutableStateFlow("")

    val searchQuery: StateFlow<String> =
        _searchQuery.asStateFlow()

    private val _selectedCatalogItem =
        MutableStateFlow<InstrumentCatalogItem?>(null)

    val selectedCatalogItem:
            StateFlow<InstrumentCatalogItem?> =
        _selectedCatalogItem.asStateFlow()

    fun setCategory(category: String) {
        _selectedCategory.value = category
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCatalogItem(item: InstrumentCatalogItem?) {
        _selectedCatalogItem.value = item
    }

    fun toggleBookmark(item: InstrumentCatalogItem) {

        viewModelScope.launch {
            repository.toggleBookmark(item)
        }
    }

    val quoteProductName =
        MutableStateFlow("")

    val quoteQuantity =
        MutableStateFlow("50")

    val quoteDestinationCountry =
        MutableStateFlow("")

    val quoteMaterial =
        MutableStateFlow("Stainless Steel 410 / 420")

    val quoteFinish =
        MutableStateFlow("Satin Finish")

    val quoteCustomization =
        MutableStateFlow("Standard Packing")

    val quoteSpecialSpecs =
        MutableStateFlow("")

    val quoteSuccessBanner =
        MutableStateFlow<String?>(null)

    fun updateQuoteProductName(value: String) {
        quoteProductName.value = value
    }

    fun updateQuoteQuantity(value: String) {
        quoteQuantity.value = value
    }

    fun updateQuoteDestinationCountry(value: String) {
        quoteDestinationCountry.value = value
    }

    fun updateQuoteMaterial(value: String) {
        quoteMaterial.value = value
    }

    fun updateQuoteFinish(value: String) {
        quoteFinish.value = value
    }

    fun updateQuoteCustomization(value: String) {
        quoteCustomization.value = value
    }

    fun updateQuoteSpecialSpecs(value: String) {
        quoteSpecialSpecs.value = value
    }

    fun prefillQuoteForProduct(
        item: InstrumentCatalogItem
    ) {
        quoteProductName.value = item.name
        quoteMaterial.value = item.recommendedMaterial
        _selectedTab.value = 2
    }

    fun submitQuotationInquiry() {

        val name = quoteProductName.value.trim()
        val qty = quoteQuantity.value.toIntOrNull() ?: 1
        val country = quoteDestinationCountry.value.trim()

        if (name.isEmpty() || country.isEmpty()) {

            quoteSuccessBanner.value =
                "Please enter the product name and destination country."

            return
        }

        viewModelScope.launch {

            repository.saveQuotation(
                QuotationInquiry(
                    productName = name,
                    quantity = qty,
                    destinationCountry = country,
                    material = quoteMaterial.value,
                    finish = quoteFinish.value,
                    customization = quoteCustomization.value,
                    specialSpecs = quoteSpecialSpecs.value,
                    status = "Official Request Drafted"
                )
            )

            quoteSuccessBanner.value =
                "Quotation inquiry saved successfully! Available under Saved & Inquiries."

            quoteProductName.value = ""
            quoteDestinationCountry.value = ""
            quoteSpecialSpecs.value = ""
        }
    }

    val customInstrumentType =
        MutableStateFlow("")

    val customPhotoRef =
        MutableStateFlow("")

    val customDimensions =
        MutableStateFlow("")

    val customMaterialReq =
        MutableStateFlow("Japanese/German Stainless Steel")

    val customLogoBranding =
        MutableStateFlow("Laser Engraved Logo")

    val customSpecialInstructions =
        MutableStateFlow("")

    fun updateCustomInstrumentType(value: String) {
        customInstrumentType.value = value
    }

    fun updateCustomPhotoRef(value: String) {
        customPhotoRef.value = value
    }

    fun updateCustomDimensions(value: String) {
        customDimensions.value = value
    }

    fun updateCustomMaterialReq(value: String) {
        customMaterialReq.value = value
    }

    fun updateCustomLogoBranding(value: String) {
        customLogoBranding.value = value
    }

    fun updateCustomSpecialInstructions(value: String) {
        customSpecialInstructions.value = value
    }

    fun submitCustomSpecInquiry() {

        val type = customInstrumentType.value.trim()

        if (type.isEmpty()) {

            quoteSuccessBanner.value =
                "Please specify the custom instrument type."

            return
        }

        viewModelScope.launch {

            repository.saveCustomSpec(
                CustomSpecInquiry(
                    instrumentType = type,
                    photoRef = customPhotoRef.value.trim(),
                    dimensions = customDimensions.value.trim(),
                    materialRequirements = customMaterialReq.value,
                    logoBranding = customLogoBranding.value,
                    specialInstructions = customSpecialInstructions.value
                )
            )

            quoteSuccessBanner.value =
                "Custom OEM specification draft saved!"

            customInstrumentType.value = ""
            customDimensions.value = ""
            customSpecialInstructions.value = ""
        }
    }

    fun deleteQuotation(id: Long) {

        viewModelScope.launch {
            repository.deleteQuotation(id)
        }
    }

    fun deleteCustomSpec(id: Long) {

        viewModelScope.launch {
            repository.deleteCustomSpec(id)
        }
    }

    fun clearBanner() {
        quoteSuccessBanner.value = null
    }
}

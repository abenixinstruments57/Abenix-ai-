package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.BookmarkedProduct
import com.example.data.model.ChatMessage
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.QuotationInquiry
import kotlinx.coroutines.flow.Flow

@Dao
interface AbenixDao {
    @Query("SELECT * FROM chat_messages ORDER BY timestamp ASC")
    fun getAllMessages(): Flow<List<ChatMessage>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(message: ChatMessage): Long

    @Query("DELETE FROM chat_messages")
    suspend fun clearMessages()

    @Query("SELECT * FROM quotation_inquiries ORDER BY timestamp DESC")
    fun getAllQuotations(): Flow<List<QuotationInquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuotation(quotation: QuotationInquiry): Long

    @Query("DELETE FROM quotation_inquiries WHERE id = :id")
    suspend fun deleteQuotation(id: Long)

    @Query("SELECT * FROM custom_spec_inquiries ORDER BY timestamp DESC")
    fun getAllCustomSpecs(): Flow<List<CustomSpecInquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomSpec(spec: CustomSpecInquiry): Long

    @Query("DELETE FROM custom_spec_inquiries WHERE id = :id")
    suspend fun deleteCustomSpec(id: Long)

    @Query("SELECT * FROM bookmarked_products ORDER BY timestamp DESC")
    fun getBookmarkedProducts(): Flow<List<BookmarkedProduct>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookmark(product: BookmarkedProduct)

    @Query("DELETE FROM bookmarked_products WHERE id = :id")
    suspend fun deleteBookmark(id: String)

    @Query("SELECT EXISTS(SELECT 1 FROM bookmarked_products WHERE id = :id)")
    suspend fun isBookmarked(id: String): Boolean
}

package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.BookmarkedProduct
import com.example.data.model.ChatMessage
import com.example.data.model.CustomSpecInquiry
import com.example.data.model.QuotationInquiry

@Database(
    entities = [
        ChatMessage::class,
        QuotationInquiry::class,
        CustomSpecInquiry::class,
        BookmarkedProduct::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun abenixDao(): AbenixDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "abenix_instruments_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

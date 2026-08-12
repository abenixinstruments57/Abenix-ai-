package com.example.data

import android.content.Context

class AbenixSettings(context: Context) {

    private val preferences = context.getSharedPreferences(
        "abenix_settings",
        Context.MODE_PRIVATE
    )

    var companyName: String
        get() = preferences.getString(
            "company_name",
            "Abenix Instruments"
        ) ?: "Abenix Instruments"
        set(value) {
            preferences.edit()
                .putString("company_name", value)
                .apply()
        }

    var email1: String
        get() = preferences.getString(
            "email_1",
            "abenixinstruments@gmail.com"
        ) ?: "abenixinstruments@gmail.com"
        set(value) {
            preferences.edit()
                .putString("email_1", value)
                .apply()
        }

    var email2: String
        get() = preferences.getString(
            "email_2",
            "Info@abenixinstruments.com"
        ) ?: "Info@abenixinstruments.com"
        set(value) {
            preferences.edit()
                .putString("email_2", value)
                .apply()
        }

    var phone: String
        get() = preferences.getString(
            "phone",
            "03025526011"
        ) ?: "03025526011"
        set(value) {
            preferences.edit()
                .putString("phone", value)
                .apply()
        }

    var instagram: String
        get() = preferences.getString(
            "instagram",
            "https://www.instagram.com/invites/contact/?utm_source=ig_contact_invite&utm_medium=copy_link&utm_content=kl7m992"
        ) ?: ""
        set(value) {
            preferences.edit()
                .putString("instagram", value)
                .apply()
        }

    var aiInstructions: String
        get() = preferences.getString(
            "ai_instructions",
            "You are Abenix AI, the professional AI assistant for Abenix Instruments. Give specific, useful answers about surgical instruments. Do not repeat the same generic response. Answer according to the user's actual question."
        ) ?: ""
        set(value) {
            preferences.edit()
                .putString("ai_instructions", value)
                .apply()
        }
}

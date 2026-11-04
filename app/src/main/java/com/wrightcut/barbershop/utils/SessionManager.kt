package com.wrightcut.barbershop.utils

import android.content.Context
import android.content.SharedPreferences

object SessionManager {
    private const val PREF_NAME = "wright_cut_session"
    private const val KEY_IS_LOGGED_IN = "is_logged_in"
    private const val KEY_LAST_ACTIVE = "last_active_timestamp"
    private const val KEY_USER_EMAIL = "user_email"

    private const val TWO_HOURS_MILLIS = 2 * 60 * 60 * 1000L

    private val ADMIN_EMAILS = listOf("kagisogeorge09@gmail.com", "boikhutso@wrightcut.co.za", "admin@wrightcut.co.za")

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun startSession(context: Context, email: String) {
        getPrefs(context).edit().apply {
            putBoolean(KEY_IS_LOGGED_IN, true)
            putLong(KEY_LAST_ACTIVE, System.currentTimeMillis())
            putString(KEY_USER_EMAIL, email.lowercase().trim())
            apply()
        }
    }

    fun updateActivity(context: Context) {
        val prefs = getPrefs(context)
        if (prefs.getBoolean(KEY_IS_LOGGED_IN, false)) {
            prefs.edit().putLong(KEY_LAST_ACTIVE, System.currentTimeMillis()).apply()
        }
    }

    fun isSessionValid(context: Context): Boolean {
        val prefs = getPrefs(context)
        val isLoggedIn = prefs.getBoolean(KEY_IS_LOGGED_IN, false)
        if (!isLoggedIn) return false

        val lastActive = prefs.getLong(KEY_LAST_ACTIVE, 0L)
        val elapsed = System.currentTimeMillis() - lastActive

        return if (elapsed > TWO_HOURS_MILLIS) {
            clearSession(context)
            false
        } else {
            updateActivity(context)
            true
        }
    }

    fun getUserEmail(context: Context): String {
        return getPrefs(context).getString(KEY_USER_EMAIL, "") ?: ""
    }

    fun isAdmin(context: Context): Boolean {
        val email = getUserEmail(context).lowercase().trim()
        return ADMIN_EMAILS.any { it.equals(email, ignoreCase = true) }
    }

    fun clearSession(context: Context) {
        getPrefs(context).edit().clear().apply()
    }
}
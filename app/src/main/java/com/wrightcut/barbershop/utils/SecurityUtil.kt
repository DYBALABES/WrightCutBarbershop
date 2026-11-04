package com.wrightcut.barbershop.utils

import java.security.MessageDigest

object SecurityUtil {
    private const val SALT = "WrightCutSalt2026Secure"

    /**
     * Salts and hashes user passwords using SHA-256 for secure storage.
     * Guarantees that passwords are never stored or transmitted in plain text.
     */
    fun hashPassword(password: String): String {
        val inputWithSalt = password + SALT
        val bytes = MessageDigest.getInstance("SHA-256").digest(inputWithSalt.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Validates that the input matches standard email format.
     */
    fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches()
    }

    /**
     * Validates that the password is at least 6 characters long.
     */
    fun isValidPassword(password: String): Boolean {
        return password.trim().length >= 6
    }
}
package com.wrightcut.barbershop.utils

import java.security.MessageDigest

object SecurityUtil {
    private const val SALT = "WrightCutSecureSalt2026"
    private val EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

    /**
     * Salts and hashes user passwords using SHA-256 for secure storage.
     */
    fun hashPassword(password: String): String {
        val inputWithSalt = password + SALT
        val bytes = MessageDigest.getInstance("SHA-256").digest(inputWithSalt.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Validates that the input matches an email pattern.
     */
    fun isValidEmail(email: String): Boolean {
        return email.trim().matches(EMAIL_REGEX)
    }

    /**
     * Validates that password meets minimum length requirement (>= 6 characters).
     */
    fun isValidPassword(password: String): Boolean {
        return password.trim().length >= 6
    }
}
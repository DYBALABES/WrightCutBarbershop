package com.wrightcut.barbershop

import com.wrightcut.barbershop.utils.SecurityUtil
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BarberUnitTests {

    // POSITIVE TEST CASES (GOOD INPUT)

    @Test
    fun emailValidation_validFormat_returnsTrue() {
        val goodEmail = "kagiso.maluleka@students.iie.ac.za"
        val result = SecurityUtil.isValidEmail(goodEmail)
        assertTrue("Valid email format should pass validation", result)
    }

    @Test
    fun passwordValidation_validLength_returnsTrue() {
        val strongPass = "MasterBarber2026!"
        val result = SecurityUtil.isValidPassword(strongPass)
        assertTrue("Password with 6 or more characters should return true", result)
    }

    @Test
    fun passwordHashing_identicalInputs_produceIdenticalDigests() {
        val rawPassword = "WrightCutSecureKey2026"
        val hash1 = SecurityUtil.hashPassword(rawPassword)
        val hash2 = SecurityUtil.hashPassword(rawPassword)
        assertEquals("Identical passwords must always produce matching hashed digests", hash1, hash2)
    }


    @Test
    fun emailValidation_missingDomain_returnsFalse() {
        val malformedEmail = "plain-string-without-at-symbol"
        val result = SecurityUtil.isValidEmail(malformedEmail)
        assertFalse("Malformed email lacking @ and domain must fail validation", result)
    }

    @Test
    fun passwordValidation_tooShort_returnsFalse() {
        val shortPassword = "123"
        val result = SecurityUtil.isValidPassword(shortPassword)
        assertFalse("Password with less than 6 characters must fail validation", result)
    }

    @Test
    fun passwordHashing_differentPasswords_produceDifferentHashes() {
        val hashAlpha = SecurityUtil.hashPassword("PasswordAlpha")
        val hashBeta = SecurityUtil.hashPassword("PasswordBeta")
        assertNotEquals("Different passwords must never yield the same cryptographic hash", hashAlpha, hashBeta)
    }
}
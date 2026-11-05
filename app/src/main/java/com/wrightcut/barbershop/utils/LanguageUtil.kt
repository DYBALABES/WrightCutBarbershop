package com.wrightcut.barbershop.utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

object LanguageUtil {
    private val languages = arrayOf(
        "English",
        "Afrikaans",
        "Sesotho",
        "isiZulu",
        "isiXhosa"
    )
    private val localeCodes = arrayOf("en", "af", "st", "zu", "xh")

    fun showLanguageDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Select Language / Khetha Ulimi")
            .setItems(languages) { _, index ->
                val code = localeCodes[index]
                val appLocale = LocaleListCompat.forLanguageTags(code)
                AppCompatDelegate.setApplicationLocales(appLocale)
                Toast.makeText(context, "Language set to ${languages[index]}", Toast.LENGTH_SHORT).show()

                // Immediately refresh activity so all text changes instantly
                if (context is Activity) {
                    context.recreate()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
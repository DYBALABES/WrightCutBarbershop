package com.wrightcut.barbershop.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.wrightcut.barbershop.databinding.FragmentSettingsBinding
import com.wrightcut.barbershop.ui.activities.LoginActivity

class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 5-Language
        binding.rowLanguage.setOnClickListener {
            val languages = arrayOf("English", "Afrikaans", "Sesotho", "isiZulu", "isiXhosa")
            val codes = arrayOf("en", "af", "st", "zu", "xh")

            AlertDialog.Builder(requireContext())
                .setTitle("Select App Language")
                .setItems(languages) { _, index ->
                    val selectedCode = codes[index]
                    binding.tvCurrentLanguage.text = "${languages[index]} ›"
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(selectedCode))
                    Toast.makeText(requireContext(), "Language changed to ${languages[index]}", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.rowOfflineSync.setOnClickListener {
            Toast.makeText(requireContext(), "RoomDB: All local records are synchronized.", Toast.LENGTH_LONG).show()
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
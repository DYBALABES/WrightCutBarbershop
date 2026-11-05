package com.wrightcut.barbershop.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.wrightcut.barbershop.R
import com.wrightcut.barbershop.databinding.FragmentServicesBinding
import com.wrightcut.barbershop.ui.activities.BookAppointmentActivity
import com.wrightcut.barbershop.ui.activities.LoginActivity
import com.wrightcut.barbershop.utils.LanguageUtil
import com.wrightcut.barbershop.utils.SessionManager

class ServicesFragment : Fragment() {
    private var _binding: FragmentServicesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentServicesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDynamicUserInitials()

        binding.btnTopMenu.setOnClickListener {
            val popup = PopupMenu(requireContext(), binding.btnTopMenu)
            popup.menu.add(getString(R.string.menu_settings))
            popup.menu.add(getString(R.string.menu_sign_out))

            popup.setOnMenuItemClickListener { item ->
                when (item.title) {
                    getString(R.string.menu_settings) -> {
                        LanguageUtil.showLanguageDialog(requireActivity())
                        true
                    }
                    getString(R.string.menu_sign_out) -> {
                        signOutUser()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }

        binding.tvUserInitials.setOnClickListener {
            LanguageUtil.showLanguageDialog(requireActivity())
        }

        binding.btnProceedToBook.setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
        }

        binding.pillHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
        }

        binding.pillGallery.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, GalleryFragment())
                .commit()
        }

        binding.pillBook.setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
        }

        binding.pillAbout.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, AboutFragment())
                .commit()
        }

        binding.pillSignOut.setOnClickListener {
            signOutUser()
        }

        binding.btnWhatsApp.setOnClickListener {
            val phone = "+27685996843"
            val url = "https://api.whatsapp.com/send?phone=$phone&text=Hi%20Wright%20Cut%20Barbershop!"
            try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) } catch (e: Exception) {}
        }

        binding.btnInstagram.setOnClickListener { Toast.makeText(requireContext(), "Opening Instagram @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnFacebook.setOnClickListener { Toast.makeText(requireContext(), "Opening Facebook @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTikTok.setOnClickListener { Toast.makeText(requireContext(), "Opening TikTok @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTwitter.setOnClickListener { Toast.makeText(requireContext(), "Opening X @wrightcut", Toast.LENGTH_SHORT).show() }
    }

    private fun signOutUser() {
        SessionManager.clearSession(requireContext())
        try { FirebaseAuth.getInstance().signOut() } catch (e: Exception) {}
        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun setupDynamicUserInitials() {
        val email = SessionManager.getUserEmail(requireContext())
        binding.tvUserInitials.text = if (email.isNotBlank()) email.take(2).uppercase() else "WC"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
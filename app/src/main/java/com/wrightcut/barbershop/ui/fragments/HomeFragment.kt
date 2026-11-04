package com.wrightcut.barbershop.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.wrightcut.barbershop.R
import com.wrightcut.barbershop.databinding.FragmentHomeBinding
import com.wrightcut.barbershop.ui.activities.AdminAppointmentsActivity
import com.wrightcut.barbershop.ui.activities.BookAppointmentActivity
import com.wrightcut.barbershop.ui.activities.LoginActivity
import com.wrightcut.barbershop.utils.LanguageUtil
import com.wrightcut.barbershop.utils.SessionManager

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupDynamicUserInitials()

        // Language Selector: opens dialog to switch language live
        binding.pillLanguage.setOnClickListener {
            LanguageUtil.showLanguageDialog(requireContext())
        }

        // Tapping the avatar circle also opens language settings
        binding.tvUserInitials.setOnClickListener {
            LanguageUtil.showLanguageDialog(requireContext())
        }

        // Admin Panel visibility
        if (SessionManager.isAdmin(requireContext())) {
            binding.pillAdmin.visibility = View.VISIBLE
            binding.pillAdmin.setOnClickListener {
                startActivity(Intent(requireContext(), AdminAppointmentsActivity::class.java))
            }
        } else {
            binding.pillAdmin.visibility = View.GONE
        }

        binding.btnBookBarberSession.setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
        }

        binding.pillServices.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, ServicesFragment())
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
            SessionManager.clearSession(requireContext())
            try { FirebaseAuth.getInstance().signOut() } catch (e: Exception) {}
            startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
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

    private fun setupDynamicUserInitials() {
        val email = SessionManager.getUserEmail(requireContext())
        binding.tvUserInitials.text = if (email.isNotBlank()) email.take(2).uppercase() else "WC"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
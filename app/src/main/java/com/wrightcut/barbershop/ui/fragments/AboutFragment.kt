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
import com.wrightcut.barbershop.databinding.FragmentAboutBinding
import com.wrightcut.barbershop.ui.activities.BookAppointmentActivity
import com.wrightcut.barbershop.ui.activities.LoginActivity

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         setupDynamicUserInitials()

        // Google Maps
        binding.btnOpenMaps.setOnClickListener {
            val addressUri = Uri.parse("geo:0,0?q=932+Monato+Street,+Ga-Rankuwa+View,+Zone+15,+Gauteng")
            val mapIntent = Intent(Intent.ACTION_VIEW, addressUri).apply {
                setPackage("com.google.android.apps.maps")
            }
            try {
                startActivity(mapIntent)
            } catch (e: Exception) {
                val browserUri = Uri.parse("https://maps.google.com/?q=932+Monato+Street,+Ga-Rankuwa+View,+Zone+15,+Gauteng")
                startActivity(Intent(Intent.ACTION_VIEW, browserUri))
            }
        }

        // Top Navigation Pills
        binding.pillHome.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, HomeFragment())
                .commit()
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

        binding.pillSignOut.setOnClickListener {
            try {
                FirebaseAuth.getInstance().signOut()
            } catch (e: Exception) {
                // Ignore in demo mode
            }
            val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }

        // WhatsApp direct link
        binding.btnWhatsApp.setOnClickListener {
            val phone = "+27685996843"
            val url = "https://api.whatsapp.com/send?phone=$phone&text=Hi%20Wright%20Cut%20Barbershop!"
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                Toast.makeText(requireContext(), "WhatsApp: 068 599 6843", Toast.LENGTH_SHORT).show()
            }
        }

        // Social Media toasts
        binding.btnInstagram.setOnClickListener { Toast.makeText(requireContext(), "Opening Instagram @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnFacebook.setOnClickListener { Toast.makeText(requireContext(), "Opening Facebook @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTikTok.setOnClickListener { Toast.makeText(requireContext(), "Opening TikTok @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTwitter.setOnClickListener { Toast.makeText(requireContext(), "Opening X @wrightcut", Toast.LENGTH_SHORT).show() }
    }

    private fun setupDynamicUserInitials() {
        try {
            val user = FirebaseAuth.getInstance().currentUser
            val displayName = user?.displayName
            val email = user?.email

            val initials = when {
                !displayName.isNullOrBlank() -> {
                    val parts = displayName.trim().split(" ").filter { it.isNotBlank() }
                    if (parts.size >= 2) "${parts[0].first()}${parts[1].first()}".uppercase()
                    else parts[0].take(2).uppercase()
                }
                !email.isNullOrBlank() -> {
                    email.substringBefore("@").take(2).uppercase()
                }
                else -> "WC"
            }
            binding.tvUserInitials.text = initials
        } catch (e: Exception) {
            binding.tvUserInitials.text = "WC"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
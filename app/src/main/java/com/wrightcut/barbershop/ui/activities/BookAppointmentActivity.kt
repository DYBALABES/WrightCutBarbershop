package com.wrightcut.barbershop.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wrightcut.barbershop.MainActivity
import com.wrightcut.barbershop.R
import com.wrightcut.barbershop.data.local.DatabaseHelper
import com.wrightcut.barbershop.databinding.ActivityBookAppointmentBinding
import com.wrightcut.barbershop.utils.SessionManager
import java.util.Calendar

class BookAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBookAppointmentBinding
    private var selectedTimeSlot = "10:00 AM"

    private val servicesList = arrayOf(
        "Fade Only - R100",
        "Fade + Black Dye - R150",
        "Chiskop (Clipper) - R80",
        "Chiskop (Blade) - R100",
        "Clipper Shave - R80",
        "Blade Shave - R100",
        "Beard Black Colour - R80",
        "Big Design - R150",
        "Trim - R50"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDynamicUserInitials()

        // Admin Panel navigation
        if (SessionManager.isAdmin(this)) {
            binding.pillAbout.text = "Admin Panel"
            binding.pillAbout.setOnClickListener {
                startActivity(Intent(this, AdminAppointmentsActivity::class.java))
                finish()
            }
        } else {
            binding.pillAbout.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java).apply { putExtra("OPEN_TAB", "ABOUT") }
                startActivity(intent)
                finish()
            }
        }

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, servicesList)
        binding.spSelectedService.adapter = adapter

        intent.getStringExtra("SELECTED_SERVICE")?.let { selected ->
            val index = servicesList.indexOfFirst { it.startsWith(selected, ignoreCase = true) }
            if (index >= 0) binding.spSelectedService.setSelection(index)
        }

        val userEmail = SessionManager.getUserEmail(this)
        if (userEmail.isNotBlank()) {
            binding.etCustomerEmail.setText(userEmail)
        }

        binding.etSelectDate.setOnClickListener {
            val c = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                binding.etSelectDate.setText(formattedDate)
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
        }

        val slotButtons = listOf(
            binding.slot08, binding.slot09, binding.slot10,
            binding.slot11, binding.slot12, binding.slot14,
            binding.slot15, binding.slot16, binding.slot17
        )

        fun highlightSlot(selected: TextView) {
            selectedTimeSlot = selected.text.toString()
            slotButtons.forEach { slot ->
                if (slot == selected) {
                    slot.setBackgroundResource(R.drawable.shape_gold_btn)
                    slot.setTextColor(getColor(R.color.bg_dark))
                } else {
                    slot.setBackgroundResource(R.drawable.shape_dark_card)
                    slot.setTextColor(getColor(R.color.text_white))
                }
            }
        }

        highlightSlot(binding.slot10)

        slotButtons.forEach { slot ->
            slot.setOnClickListener { highlightSlot(slot) }
        }

        binding.tabMyAppointments.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OPEN_TAB", "BOOKINGS")
            }
            startActivity(intent)
            finish()
        }

        binding.pillHome.setOnClickListener { startActivity(Intent(this, MainActivity::class.java)); finish() }
        binding.pillServices.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply { putExtra("OPEN_TAB", "SERVICES") }
            startActivity(intent)
            finish()
        }
        binding.pillGallery.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply { putExtra("OPEN_TAB", "GALLERY") }
            startActivity(intent)
            finish()
        }
        binding.pillSignOut.setOnClickListener {
            SessionManager.clearSession(this)
            try { FirebaseAuth.getInstance().signOut() } catch (e: Exception) {}
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        // Confirm & Submit
        binding.btnConfirmSubmit.setOnClickListener {
            var custEmail = binding.etCustomerEmail.text.toString().trim()
            if (custEmail.isEmpty()) custEmail = SessionManager.getUserEmail(this).ifBlank { "client@wrightcut.co.za" }

            var phone = binding.etCustomerMobile.text.toString().trim()
            if (phone.isEmpty()) phone = "0685996843"

            val service = binding.spSelectedService.selectedItem.toString()

            var date = binding.etSelectDate.text.toString().trim()
            if (date.isEmpty()) date = "2026-10-24"

            val db = DatabaseHelper(this)
            val newId = db.insertBooking(custEmail, phone, service, date, selectedTimeSlot, "Pending")

            try {
                val firestoreMap = hashMapOf(
                    "email" to custEmail,
                    "phone" to phone,
                    "service" to service,
                    "date" to date,
                    "time" to selectedTimeSlot,
                    "status" to "Pending"
                )
                FirebaseFirestore.getInstance().collection("bookings").add(firestoreMap)
            } catch (e: Exception) {}

            Toast.makeText(this, "Booking Submitted! Status: Pending Approval", Toast.LENGTH_LONG).show()

            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("OPEN_TAB", "BOOKINGS")
            }
            startActivity(intent)
            finish()
        }

        binding.btnWhatsApp.setOnClickListener {
            val p = "+27685996843"
            val url = "https://api.whatsapp.com/send?phone=$p&text=Hi%20Wright%20Cut%20Barbershop!"
            try { startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url))) } catch (e: Exception) {}
        }

        binding.btnInstagram.setOnClickListener { Toast.makeText(this, "Opening Instagram @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnFacebook.setOnClickListener { Toast.makeText(this, "Opening Facebook @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTikTok.setOnClickListener { Toast.makeText(this, "Opening TikTok @wrightcut", Toast.LENGTH_SHORT).show() }
        binding.btnTwitter.setOnClickListener { Toast.makeText(this, "Opening X @wrightcut", Toast.LENGTH_SHORT).show() }
    }

    private fun setupDynamicUserInitials() {
        val email = SessionManager.getUserEmail(this)
        binding.tvUserInitials.text = if (email.isNotBlank()) email.take(2).uppercase() else "WC"
    }
}
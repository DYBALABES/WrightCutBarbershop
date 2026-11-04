package com.wrightcut.barbershop.ui.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.wrightcut.barbershop.MainActivity
import com.wrightcut.barbershop.R
import com.wrightcut.barbershop.data.local.AdminAppointment
import com.wrightcut.barbershop.data.local.DatabaseHelper
import com.wrightcut.barbershop.databinding.ActivityAdminAppointmentsBinding
import com.wrightcut.barbershop.databinding.ItemAdminAppointmentBinding
import com.wrightcut.barbershop.utils.SessionManager
import java.util.Calendar

class AdminAppointmentsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminAppointmentsBinding
    private val appointmentsList = mutableListOf<AdminAppointment>()
    private lateinit var adapter: AdminAppointmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAppointmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupManagerHeader()

        binding.rvAdminAppointments.layoutManager = LinearLayoutManager(this)
        adapter = AdminAppointmentsAdapter(appointmentsList) { appointment, anchorView ->
            showActionMenu(appointment, anchorView)
        }
        binding.rvAdminAppointments.adapter = adapter

        // View Main Site
        binding.pillViewMainSite.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        binding.pillAdminSignOut.setOnClickListener {
            SessionManager.clearSession(this)
            try { FirebaseAuth.getInstance().signOut() } catch (e: Exception) {}
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            })
            finish()
        }

        loadAppointments()
    }

    override fun onResume() {
        super.onResume()
        loadAppointments()
    }

    private fun setupManagerHeader() {
        val email = SessionManager.getUserEmail(this).ifBlank { "kagisogeorge09@gmail.com" }
        binding.tvManagerEmail.text = "Manager: $email"
        binding.tvAdminInitials.text = if (email.isNotBlank()) email.take(2).uppercase() else "KM"
    }

    private fun loadAppointments() {
        appointmentsList.clear()

        val db = DatabaseHelper(this)
        val localList = db.getAllAdminAppointments()

        if (localList.isNotEmpty()) {
            appointmentsList.addAll(localList)
        } else {
            // Only seed sample bookings if the database is 100% empty
            db.insertBooking("zamaluleka06@gmail.com", "0605700719", "Chiskop with Blade (R70)", "2026-10-22", "09:00 AM", "Accepted")
            db.insertBooking("kagisogeorge09@gmail.com", "0685996843", "Fade Only (R100)", "2026-10-23", "11:00 AM", "Pending")
            appointmentsList.addAll(db.getAllAdminAppointments())
        }
        adapter.notifyDataSetChanged()
    }

    private fun showActionMenu(appointment: AdminAppointment, anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menu.add("Accept Appointment")
        popup.menu.add("Reschedule")
        popup.menu.add("Decline Appointment")

        popup.setOnMenuItemClickListener { item ->
            val db = DatabaseHelper(this)
            when (item.title) {
                "Accept Appointment" -> {
                    appointment.status = "Accepted"
                    db.updateBookingStatus(appointment.id, "Accepted")
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Accepted appointment for ${appointment.email}", Toast.LENGTH_SHORT).show()
                    true
                }
                "Reschedule" -> {
                    val c = Calendar.getInstance()
                    DatePickerDialog(this, { _, year, month, dayOfMonth ->
                        val newDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                        appointment.dateTime = "$newDate @ 10:00 AM"
                        appointment.status = "Rescheduled"
                        db.updateBookingStatus(appointment.id, "Rescheduled")
                        adapter.notifyDataSetChanged()
                        Toast.makeText(this, "Rescheduled to $newDate", Toast.LENGTH_SHORT).show()
                    }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show()
                    true
                }
                "Decline Appointment" -> {
                    appointment.status = "Declined"
                    db.updateBookingStatus(appointment.id, "Declined")
                    adapter.notifyDataSetChanged()
                    Toast.makeText(this, "Declined appointment for ${appointment.email}", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
        popup.show()
    }
}

class AdminAppointmentsAdapter(
    private val items: List<AdminAppointment>,
    private val onActionClick: (AdminAppointment, View) -> Unit
) : RecyclerView.Adapter<AdminAppointmentsAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemAdminAppointmentBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemAdminAppointmentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvAdminClientEmail.text = item.email
        holder.binding.tvAdminClientPhone.text = item.phone
        holder.binding.tvAdminService.text = item.service
        holder.binding.tvAdminDateTime.text = item.dateTime
        holder.binding.tvAdminStatus.text = item.status

        when (item.status.lowercase()) {
            "accepted" -> {
                holder.binding.tvAdminStatus.setTextColor(holder.itemView.context.getColor(R.color.success_green))
            }
            "declined" -> {
                holder.binding.tvAdminStatus.setTextColor(holder.itemView.context.getColor(R.color.error_red))
            }
            else -> {
                holder.binding.tvAdminStatus.setTextColor(holder.itemView.context.getColor(R.color.gold_primary))
            }
        }

        holder.binding.btnAdminActions.setOnClickListener {
            onActionClick(item, holder.binding.btnAdminActions)
        }
    }

    override fun getItemCount() = items.size
}
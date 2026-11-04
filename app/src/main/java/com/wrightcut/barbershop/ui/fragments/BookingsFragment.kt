package com.wrightcut.barbershop.ui.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wrightcut.barbershop.R
import com.wrightcut.barbershop.data.local.DatabaseHelper
import com.wrightcut.barbershop.data.local.LocalBooking
import com.wrightcut.barbershop.databinding.FragmentBookingsBinding
import com.wrightcut.barbershop.ui.activities.BookAppointmentActivity
import com.wrightcut.barbershop.utils.SessionManager

class BookingsFragment : Fragment() {
    private var _binding: FragmentBookingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvBookings.layoutManager = LinearLayoutManager(requireContext())

        // New Booking
        binding.tabNewBooking.setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
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

        loadBookings()
    }

    private fun loadBookings() {
        val email = SessionManager.getUserEmail(requireContext())
        val db = DatabaseHelper(requireContext())
        var list = db.getUserBookings(email)

        if (list.isEmpty()) {
            db.insertBooking("zamaluleka06@gmail.com", "0605700719", "Chiskop with Blade (R70)", "2026/10/22", "09:00 AM", "Accepted")
            list = db.getUserBookings(email)
        }

        binding.rvBookings.adapter = BookingAdapter(list,
            onReschedule = { booking ->
                Toast.makeText(requireContext(), "Reschedule requested for ${booking.serviceName}", Toast.LENGTH_SHORT).show()
            },
            onCancel = { booking ->
                db.updateBookingStatus(booking.localId.toString(), "Cancelled")
                loadBookings()
                Toast.makeText(requireContext(), "Cancelled ${booking.serviceName}", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class BookingAdapter(
    private val items: List<LocalBooking>,
    private val onReschedule: (LocalBooking) -> Unit,
    private val onCancel: (LocalBooking) -> Unit
) : RecyclerView.Adapter<BookingAdapter.ViewHolder>() {

    class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val service: TextView = v.findViewById(R.id.tvBookingService)
        val status: TextView = v.findViewById(R.id.tvBookingStatus)
        val details: TextView = v.findViewById(R.id.tvBookingDetails)
        val btnReschedule: Button = v.findViewById(R.id.btnReschedule)
        val btnCancel: Button = v.findViewById(R.id.btnCancel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.service.text = item.serviceName
        holder.status.text = item.status
        holder.details.text = "${item.appointmentDate} at ${item.appointmentTime} · R${item.price.toInt()}"

        holder.btnReschedule.setOnClickListener { onReschedule(item) }
        holder.btnCancel.setOnClickListener { onCancel(item) }
    }

    override fun getItemCount() = items.size
}
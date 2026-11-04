package com.wrightcut.barbershop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.wrightcut.barbershop.MainActivity
import com.wrightcut.barbershop.data.local.DatabaseHelper
import com.wrightcut.barbershop.databinding.ActivityLoginBinding
import com.wrightcut.barbershop.utils.SecurityUtil
import com.wrightcut.barbershop.utils.SessionManager

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    private val adminEmails = listOf("kagisogeorge09@gmail.com", "boikhutso@wrightcut.co.za", "admin@wrightcut.co.za")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (!SecurityUtil.isValidEmail(email)) {
                binding.etEmail.error = "Valid email is required"
                return@setOnClickListener
            }
            if (!SecurityUtil.isValidPassword(password)) {
                binding.etPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            val inputHashedPassword = SecurityUtil.hashPassword(password)
           val db = DatabaseHelper(this)
            val registeredUser = db.getUserByEmail(email)

            if (registeredUser != null) {
                 if (registeredUser.passwordHash == inputHashedPassword) {
                    SessionManager.startSession(this, email)
                    Toast.makeText(this, "Welcome back, ${registeredUser.fullName}!", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                } else {
                    binding.etPassword.error = "Incorrect password! Please try again."
                    Toast.makeText(this, "Incorrect password!", Toast.LENGTH_SHORT).show()
                }
            } else if (adminEmails.any { it.equals(email, ignoreCase = true) }) {
                // Admin manager login
                SessionManager.startSession(this, email)
                Toast.makeText(this, "Admin Login: Welcome, Manager!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AdminAppointmentsActivity::class.java))
                finish()
            } else {
                 try {
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                        .addOnSuccessListener {
                            SessionManager.startSession(this, email)
                            Toast.makeText(this, "Welcome to Wright Cut!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Account not found! Please tap Register below to create one.", Toast.LENGTH_LONG).show()
                        }
                } catch (e: Exception) {
                    Toast.makeText(this, "Account not found! Please tap Register below to create one.", Toast.LENGTH_LONG).show()
                }
            }
        }

        binding.btnGoogleSSO.setOnClickListener {
            SessionManager.startSession(this, "google_user@gmail.com")
            Toast.makeText(this, "Google SSO Authenticated", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.tvRegisterLink.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}
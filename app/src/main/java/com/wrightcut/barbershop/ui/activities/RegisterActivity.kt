package com.wrightcut.barbershop.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wrightcut.barbershop.MainActivity
import com.wrightcut.barbershop.data.local.DatabaseHelper
import com.wrightcut.barbershop.data.local.LocalUser
import com.wrightcut.barbershop.databinding.ActivityRegisterBinding
import com.wrightcut.barbershop.utils.SecurityUtil
import com.wrightcut.barbershop.utils.SessionManager

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val name = binding.etFullName.text.toString().trim()
            val email = binding.etRegEmail.text.toString().trim().lowercase()
            val phone = binding.etRegPhone.text.toString().trim()
            val password = binding.etRegPassword.text.toString().trim()

            if (name.isEmpty()) {
                binding.etFullName.error = "Full Name is required"
                return@setOnClickListener
            }
            if (!SecurityUtil.isValidEmail(email)) {
                binding.etRegEmail.error = "Valid email is required"
                return@setOnClickListener
            }
            if (!SecurityUtil.isValidPassword(password)) {
                binding.etRegPassword.error = "Password must be at least 6 characters"
                return@setOnClickListener
            }

            val hashedPassword = SecurityUtil.hashPassword(password)

            val localUser = LocalUser(
                email = email,
                fullName = name,
                phone = phone,
                passwordHash = hashedPassword
            )


            val db = DatabaseHelper(this)
            db.insertUser(localUser)

             try {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: ""
                        val profile = hashMapOf(
                            "userId" to uid,
                            "fullName" to name,
                            "email" to email,
                            "phone" to phone,
                            "passwordHash" to hashedPassword
                        )
                        FirebaseFirestore.getInstance().collection("users").document(uid).set(profile)
                    }
            } catch (e: Exception) {
            }

             SessionManager.startSession(this, email)
            Toast.makeText(this, "Account registered & saved successfully!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.tvLoginLink.setOnClickListener {
            finish()
        }
    }
}
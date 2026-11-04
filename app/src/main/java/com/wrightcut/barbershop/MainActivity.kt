package com.wrightcut.barbershop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.wrightcut.barbershop.ui.fragments.AboutFragment
import com.wrightcut.barbershop.ui.fragments.BookingsFragment
import com.wrightcut.barbershop.ui.fragments.GalleryFragment
import com.wrightcut.barbershop.ui.fragments.HomeFragment
import com.wrightcut.barbershop.ui.fragments.ServicesFragment
import com.wrightcut.barbershop.ui.fragments.SettingsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            val openTab = intent.getStringExtra("OPEN_TAB")
            when (openTab) {
                "BOOKINGS" -> navigateTo(BookingsFragment())
                "SERVICES" -> navigateTo(ServicesFragment())
                "GALLERY" -> navigateTo(GalleryFragment())
                "ABOUT" -> navigateTo(AboutFragment())
                "SETTINGS" -> navigateTo(SettingsFragment())
                else -> navigateTo(HomeFragment())
            }
        }
    }

    fun navigateTo(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, fragment)
            .commit()
    }
}
package com.amtech.baseetcustomer.MainActivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.amtech.baseetcustomer.R
import com.amtech.baseetcustomer.databinding.ActivityMainBinding
import com.example.instantapp.sharedpreferences.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private var context = this@MainActivity
    lateinit var sessionManager: SessionManager
    var count = 0
    var countDes = 0
    private lateinit var bottomNav: BottomNavigationView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        sessionManager = SessionManager(context)
        bottomNav = binding.bottomNavigationView
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragment)
        val navController = navHostFragment!!.findNavController()
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bootom_nav_menu)
        binding.bottomNavigationView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragment_Home -> {
                    binding.tvTitle.text = "Home"

                }

                R.id.fragment_myBooking -> {
                    binding.tvTitle.text = "My Bookings"

                }

                R.id.fragment_Notification -> {
                    binding.tvTitle.text = "Notifications"

                }

                R.id.fragment_Profile -> {
                    binding.tvTitle.text = "Profile"

                }

                else -> {

                }
            }
        }

    }
}
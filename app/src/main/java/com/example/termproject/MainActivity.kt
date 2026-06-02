package com.example.termproject

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.termproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bottomNavigation = binding.bottomNavigation

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_travel_list -> {
                    replaceFragment(TravelListFragment())
                    true
                }
                R.id.menu_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment is TravelListFragment) {
                bottomNavigation.menu.findItem(R.id.menu_travel_list).isChecked = true
            } else if (currentFragment is SettingsFragment) {
                bottomNavigation.menu.findItem(R.id.menu_settings).isChecked = true
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment?.javaClass == fragment.javaClass) return

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
package com.example.laki_hero.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.laki_hero.R
import com.example.laki_hero.databinding.ActivityMainBinding
import com.example.laki_hero.viewmodel.MainViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private var state: String = HOME // Inisialisasi dengan nilai awal
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain FCM token
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                // Send token to your server or use it directly in PHP script
                println("FCM Token: $token")
//                Toast.makeText(this, "Token" + token, Toast.LENGTH_LONG).show()
            }
        }

//        viewModel.getSession().observe(this){
//            if (!it.isLogin){
//                val intent = Intent(this, SignInActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//                startActivity(intent)
//                finish()
//            }
//        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navView.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    when (state) {
                        RIWAYAT -> navController.navigate(R.id.action_navigation_riwayat_to_navigation_home)
                        REWARD -> navController.navigate(R.id.action_navigation_reward_to_navigation_home)
                        ABOUT -> navController.navigate(R.id.action_navigation_about_to_navigation_home)
                    }
                    state = HOME // Perbarui state setelah navigasi
                    return@OnItemSelectedListener true
                }
                R.id.menu_riwayat -> {
                    when (state) {
                        HOME -> navController.navigate(R.id.action_navigation_home_to_navigation_riwayat)
                        REWARD -> navController.navigate(R.id.action_navigation_reward_to_navigation_riwayat)
                        ABOUT -> navController.navigate(R.id.action_navigation_about_to_navigation_riwayat)
                    }
                    state = RIWAYAT // Perbarui state setelah navigasi
                    return@OnItemSelectedListener true
                }
                R.id.menu_reward -> {
                    when (state) {
                        HOME -> navController.navigate(R.id.action_navigation_home_to_navigation_reward)
                        RIWAYAT -> navController.navigate(R.id.action_navigation_riwayat_to_navigation_reward)
                        ABOUT -> navController.navigate(R.id.action_navigation_about_to_navigation_reward)
                    }
                    state = REWARD // Perbarui state setelah navigasi
                    return@OnItemSelectedListener true
                }
                R.id.menu_about -> {
                    when (state) {
                        HOME -> navController.navigate(R.id.action_navigation_home_to_navigation_about)
                        RIWAYAT -> navController.navigate(R.id.action_navigation_riwayat_to_navigation_about)
                        REWARD -> navController.navigate(R.id.action_navigation_reward_to_navigation_about)
                    }
                    state = ABOUT // Perbarui state setelah navigasi
                    return@OnItemSelectedListener true
                }
            }
            false
        })
    }

    companion object {
        const val HOME = "home"
        const val RIWAYAT = "riwayat"
        const val REWARD = "reward"
        const val ABOUT = "about"
    }
}

package com.example.laki_hero.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.laki_hero.R
import com.example.laki_hero.databinding.ActivityProfileMenuBinding
import com.example.laki_hero.viewmodel.ProfileMenuViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class ProfileMenuActivity : AppCompatActivity() {
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null

    private val viewModel by viewModels<ProfileMenuViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityProfileMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()
        // Initializing shared preferences
        sharedpreferences = this.getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(EMAIL_KEY, null)

        viewModel.getSession().observe(this){
            binding.emailTv.setText(emailsp)
            binding.namaTv.setText((it.email))
        }

        binding.logoutBtn.setOnClickListener { onClicklogout() }
    }

    private fun setActionBar(){
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Profil"

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onClicklogout(){
        lifecycleScope.launch {
            viewModel.logout()
            val intent = Intent(this@ProfileMenuActivity, SignInActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
    }
}
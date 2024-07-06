package com.example.laki_hero.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.laki_hero.R
import com.example.laki_hero.data.remote.response.ListLaporanSayaItem
import com.example.laki_hero.databinding.ActivityDetailLaporanBinding
import com.google.android.material.snackbar.Snackbar

@Suppress("DEPRECATION")
class DetailLaporanActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailLaporanBinding
    object LocationData {
        var latitudes: Double? = null
        var longitudes: Double? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detail = intent.getParcelableExtra<ListLaporanSayaItem>(DETAIL_LAPORAN) as ListLaporanSayaItem
        laporanDetail(detail)

        val latitudes = LocationData.latitudes
        val longitudes = LocationData.longitudes

        binding.locationBtn.setOnClickListener {
            val mapIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("google.navigation:q=$latitudes,$longitudes")
            )
            mapIntent.setPackage("com.google.android.apps.maps")

            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            } else {
                Toast.makeText(this, "Tidak ada google maps", Toast.LENGTH_LONG).show()
            }
        }

        setActionBar()
    }

    private fun setActionBar(){
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Detail laporan"
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun laporanDetail(detail: ListLaporanSayaItem){
        Glide.with(applicationContext)
            .load(detail.gambar)
            .into(binding.ivDetailPhoto)
        binding.namaTempat.text = detail.tempat
        binding.tanggalTv.text = detail.createdAt
        binding.isiDeskripsi.text = detail.deskripsi
        when (detail.status.toLowerCase()) {
            "monitored" -> binding.cvStatus.setBackgroundResource(R.drawable.background_monitored)
            "approved" -> binding.cvStatus.setBackgroundResource(R.drawable.background_approved)
            "rejected" -> binding.cvStatus.setBackgroundResource(R.drawable.background_rejected)
            else -> binding.cvStatus.setBackgroundResource(R.drawable.background_abu2)
        }
        binding.isiStatus.text = detail.status
        // Store latitude and longitude in public variables
        LocationData.latitudes = detail.latitude
        LocationData.longitudes = detail.longitude
    }

    companion object {
        const val DETAIL_LAPORAN = "detail_laporan"
    }
}
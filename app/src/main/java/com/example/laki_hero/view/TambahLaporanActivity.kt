package com.example.laki_hero.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.laki_hero.R
import com.example.laki_hero.data.remote.Result
import com.example.laki_hero.databinding.ActivityTambahLaporanBinding
import com.example.laki_hero.helper.getImageUri
import com.example.laki_hero.helper.reduceFileImage
import com.example.laki_hero.helper.uriToFile
import com.example.laki_hero.viewmodel.TambahLaporanViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

@Suppress("DEPRECATION")
class TambahLaporanActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityTambahLaporanBinding
    private var currentImageUri: Uri? = null
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null
    object LocationData {
        var latitudes: Double? = null
        var longitudes: Double? = null
    }

    // ViewModel instance
    private val viewModel by viewModels<TambahLaporanViewModel> {
        ViewModelFactory.getInstance(this)
    }

    // Permission launcher for camera permission
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
            if (isGranted) {
                Toast.makeText(this, "Permission request granted", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            }
        }

    // Check if the required permission is granted
    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            this,
            REQUIRED_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTambahLaporanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setActionBar()

        if (!allPermissionsGranted()) {
            requestPermissionLauncher.launch(REQUIRED_PERMISSION)
        }

        binding.kameraBtn.setOnClickListener { startCamera() }
        binding.submitBtn.setOnClickListener { uploadImage() }

        // Initializing shared preferences
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(EMAIL_KEY, null)

        // Initializing the map fragment
        val mapFragment = supportFragmentManager.findFragmentById(R.id.IdMap) as SupportMapFragment
        mapFragment.getMapAsync(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        Toast.makeText(this, "Email: $emailsp", Toast.LENGTH_LONG).show()
    }

    // Setting up the action bar
    private fun setActionBar() {
        setSupportActionBar(binding.topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.cross_svgrepo_com) // Assuming your close icon drawable is named cross_svgrepo_com
        supportActionBar?.title = "Buat laporan"
        binding.topAppBar.setNavigationOnClickListener {
            finish() // Close the activity when the close icon is clicked
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLocationPermission()
    }

    // Request location permission
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    // Get the current device location
    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(this.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location : Location? ->
                        location?.let {
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            mMap.addMarker(MarkerOptions().position(currentLatLng).title("Lokasi Laporan Anda"))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
                            // Store latitude and longitude in public variables
                            LocationData.latitudes = it.latitude
                            LocationData.longitudes = it.longitude
                        }
                    }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    getDeviceLocation()
                }
            }
        }
    }

    // Start the camera and capture an image
    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    // Display the captured image
    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.addLaporanIv.setImageURI(it)
        }
    }

    // Upload the captured image along with other details
    private fun uploadImage() {
        val tempat = binding.edAddTempat.text.toString()
        val deskripsi = binding.edAddDesc.text.toString()

        AlertDialog.Builder(this)
            .setTitle("Konfirmasi")
            .setMessage("Apakah Anda yakin ingin mengirim laporan ini?")
            .setPositiveButton("Ya") { _, _ ->
                // Continue upload image after confirmation
                currentImageUri?.let { uri ->
                    val file = uriToFile(uri, this).reduceFileImage()
                    Log.d("Image File", "showImage: ${file.path}")
                    val email = emailsp.toString()
                    val latitude = LocationData.latitudes
                    val longitude = LocationData.longitudes
                    Toast.makeText(this, "latitude: $latitude", Toast.LENGTH_LONG).show()
                    Toast.makeText(this, "longitude: $longitude", Toast.LENGTH_LONG).show()
//                    Toast.makeText(this, "File: $file", Toast.LENGTH_LONG).show()

                    viewModel.uploadImage(file, email, tempat, deskripsi, latitude.toString(), longitude.toString()).observe(this) { result ->
                        if (result != null) {
                            when (result) {
                                is Result.Loading -> {
                                    showLoading(true)
                                }
                                is Result.Success -> {
                                    showToast(result.data.message)
                                    showLoading(false)
                                    showMain()
                                }
                                is Result.Error -> {
                                    showToast(result.error)
                                    showLoading(false)
                                }
                            }
                        }
                    }
                } ?: showToast(getString(R.string.empty_image_warning))
            }
            .setNegativeButton("Tidak") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    // Navigate to MainActivity
    private fun showMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }

    // Show or hide the loading indicator
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    // Show a toast message
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUIRED_PERMISSION = Manifest.permission.CAMERA
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
    }
}

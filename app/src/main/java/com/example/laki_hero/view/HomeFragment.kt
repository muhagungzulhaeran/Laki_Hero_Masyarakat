package com.example.laki_hero.view

import HomeAdapter
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laki_hero.R
import com.example.laki_hero.data.remote.Result
import com.example.laki_hero.databinding.FragmentHomeBinding
import com.example.laki_hero.viewmodel.HomeViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initializing shared preferences
        sharedpreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(EMAIL_KEY, null)

        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        setupListeners()
        observeLaporan(emailsp.toString())

        // Initializing location client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        // Request location permission if not already granted
        if (!allPermissionsGranted()) {
            requestLocationPermission()
        } else {
            getDeviceLocation()
        }
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun setupListeners() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profile -> {
                    startActivity(Intent(requireActivity(), ProfileMenuActivity::class.java))
                    true
                }
                else -> false
            }
        }
        binding.fabLapor.setOnClickListener {
            startActivity(Intent(requireActivity(), TambahLaporanActivity::class.java))
        }
        binding.swipeRefreshLayout.setOnRefreshListener {
            observeLaporan(emailsp.toString()) // Refresh data when user swipes down
        }
    }

    private fun observeLaporan(email: String) {
        lifecycleScope.launch {
            viewModel.getLaporanSaya(email).observe(viewLifecycleOwner) { laporan ->
                when (laporan) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
                        val error = laporan.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        binding.swipeRefreshLayout.isRefreshing = false
                        val adapter = HomeAdapter()
                        adapter.submitList(laporan.data)
                        binding.recyclerView.adapter = adapter
                    }
                }
            }
        }
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            LOCATION_PERMISSION_REQUEST_CODE
        )
    }

    private fun getDeviceLocation() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        location?.let {
                            // Use the location here (e.g., update the UI or send it to the server)
                            val currentLatLng = LatLng(it.latitude, it.longitude)
                            // For example, store the location data or update a map
                            // ...
                        }
                    }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getDeviceLocation()
            } else {
                Toast.makeText(requireContext(), "Permission request denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

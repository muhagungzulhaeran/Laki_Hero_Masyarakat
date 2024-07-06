package com.example.laki_hero.view

import HomeAdapter
import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.laki_hero.data.remote.Result
import com.example.laki_hero.databinding.FragmentRiwayatLaporanBinding
import com.example.laki_hero.viewmodel.RiwayatLaporanViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch

class RiwayatLaporanFragment : Fragment() {

    private var _binding: FragmentRiwayatLaporanBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RiwayatLaporanViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }
    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initializing shared preferences
        sharedpreferences = requireContext().getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(EMAIL_KEY, null)

        // Inflate the layout for this fragment
        _binding = FragmentRiwayatLaporanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listLaporan(emailsp.toString())
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvRiwayat.layoutManager = layoutManager
    }

    private fun listLaporan(email: String){
        lifecycleScope.launch {
            viewModel.getRiwayatLaporanSaya(email).observe(viewLifecycleOwner) { laporan ->
                when (laporan) {
                    is  Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val error = laporan.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }

                    is  Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
                        val adapter = HomeAdapter()
                        adapter.submitList(laporan.data)
                        binding.rvRiwayat.adapter = adapter
                    }
                }
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    companion object {
        const val SHARED_PREFS = "shared_prefs"
        const val EMAIL_KEY = "email_key"
    }
}

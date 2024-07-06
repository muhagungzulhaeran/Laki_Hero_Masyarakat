package com.example.laki_hero.view

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
import com.example.laki_hero.R
import com.example.laki_hero.adapter.PoinAdapter
import com.example.laki_hero.data.remote.Result
import com.example.laki_hero.databinding.FragmentRewardBinding
import com.example.laki_hero.view.HomeFragment.Companion
import com.example.laki_hero.viewmodel.RewardViewModel
import com.example.laki_hero.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import okhttp3.Request

class RewardFragment : Fragment() {

    private lateinit var sharedpreferences: SharedPreferences
    private var emailsp: String? = null

    private var _binding: FragmentRewardBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<RewardViewModel> {
        ViewModelFactory.getInstance(requireActivity())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Initializing shared preferences
        sharedpreferences = requireContext().getSharedPreferences(HomeFragment.SHARED_PREFS, Context.MODE_PRIVATE)
        emailsp = sharedpreferences.getString(HomeFragment.EMAIL_KEY, null)


        _binding = FragmentRewardBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUI()
        println(emailsp)
        observeLaporanTransfer(emailsp.toString())
        observeLaporanPoin(emailsp.toString())
    }

    private fun observeLaporanTransfer(email: String) {

        lifecycleScope.launch {
            viewModel.getPoinSaya(email).observe(viewLifecycleOwner) { poin ->
                when (poin) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
//                        binding.swipeRefreshLayout.isRefreshing = false
                        val error = poin.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
//                        binding.swipeRefreshLayout.isRefreshing = false
                        val adapter = PoinAdapter()
                        println("data ini:")
                        println(poin.data)
                        adapter.submitList(poin.data)
                        binding.rvAktivitas.adapter = adapter
                    }
                }
            }
        }
    }

    private fun observeLaporanPoin(email: String) {
        lifecycleScope.launch {
            viewModel.getPoinDetail(email).observe(viewLifecycleOwner) { poinDetail ->
                when (poinDetail) {
                    is Result.Error -> {
                        binding.progressBar.visibility = View.INVISIBLE
//                        binding.swipeRefreshLayout.isRefreshing = false
                        val error = poinDetail.error
                        Toast.makeText(requireActivity(), error, Toast.LENGTH_SHORT).show()
                    }
                    is Result.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }
                    is Result.Success -> {
                        binding.progressBar.visibility = View.INVISIBLE
//                        binding.swipeRefreshLayout.isRefreshing = false

                        val poinDetailSaya = poinDetail.data.firstOrNull()?.poin?.toString() ?: "0"
                        println("poin  detail"+ poinDetail)
                        //Toast.makeText(requireContext(), "poin anda" + poinDetail.data.firstOrNull()?.poin?.toString() ?: "0", Toast.LENGTH_LONG).show()
                        binding.isiPoinSayaTv.text = poinDetailSaya
                    }
                }
            }
        }
    }

    private fun setupUI() {
        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvAktivitas.layoutManager = layoutManager
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
package com.example.healthme.ui.fragment.home.settings

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentLogoutBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory

class LogoutFragment : DialogFragment() {

    private var _binding: FragmentLogoutBinding? = null
    private val binding: FragmentLogoutBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]

        binding.btnYes.setOnClickListener {
            logout()
        }

        binding.btnNo.setOnClickListener {
            dialog?.cancel()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val width = ConstraintLayout.LayoutParams.WRAP_CONTENT
        val height = ConstraintLayout.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.bg_gray_20)
    }

    private fun logout() {
        viewModel.logout()
        viewModel.myResponseString.observe(this, Observer { response ->
            if (response.isSuccessful) {
                dialog?.cancel()
                findNavController().navigate(R.id.to_welcomeFragment)
            } else {
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.healthme.ui.fragment.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentLoginBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.toRegistrationBtn.setOnClickListener {
            findNavController().navigate(R.id.to_registrationFragment)
        }

        binding.btnLogin.setOnClickListener {
            login()
        }

        return binding.root
    }

    private fun login() {
        val email = binding.loginEmail.text.toString()
        val password = binding.loginPassword.text.toString()

        if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
            viewModel.login(email, password)
            viewModel.myResponseUserInfo.observe(requireActivity()) { response ->
                if (response.isSuccessful) {
                    findNavController().navigate(R.id.to_homeFragment)
                } else {
                    val errorText =
                        response.errorBody()?.string()?.substringAfter("[\"")?.dropLast(3)
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
                    Log.e("Error Response", errorText.toString())
                    Log.e("Error Response", response.code().toString())
                }
            }
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
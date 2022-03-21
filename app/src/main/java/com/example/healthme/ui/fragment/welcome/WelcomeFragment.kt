package com.example.healthme.ui.fragment.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentWelcomeBinding
import com.example.healthme.ui.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class WelcomeFragment : Fragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        (activity as MainActivity).btm_nav.visibility = View.GONE

        binding.toRegistrationBtn.setOnClickListener {
            findNavController().navigate(R.id.to_registrationFragment)
        }

        binding.toAuthorizationBtn.setOnClickListener {
            findNavController().navigate(R.id.to_loginFragment)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.healthme.ui.fragment.home.settings

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentProfileSettingsBinding

class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding: FragmentProfileSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.settings)
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)

//        binding.toCalendarBtn.setOnClickListener {
//            findNavController().navigate(R.id.to_addAppointment)
//        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
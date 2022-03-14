package com.example.healthme.ui.fragment.home.settings

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentSecuritySettingsBinding

class SecuritySettingsFragment : DialogFragment() {

    private var _binding: FragmentSecuritySettingsBinding? = null
    private val binding: FragmentSecuritySettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSecuritySettingsBinding.inflate(inflater, container, false)

        binding.layoutChangePassword.setOnClickListener {
            findNavController().navigate(R.id.to_changePasswordFragment)
        }

        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Изменения сохранены", Toast.LENGTH_LONG).show()
        }

        binding.btnCancel.setOnClickListener {
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
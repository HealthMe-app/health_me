package com.example.healthme.ui.fragment.tracker

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentAddSymptomBinding

class AddSymptomFragment : DialogFragment() {

    private var _binding: FragmentAddSymptomBinding? = null
    private val binding: FragmentAddSymptomBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSymptomBinding.inflate(inflater, container, false)

        binding.btnSave.setOnClickListener {
            Toast.makeText(requireContext(), "Запись добавлена успешно", Toast.LENGTH_LONG).show()
            dialog?.cancel()
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
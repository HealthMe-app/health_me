package com.example.healthme.ui.fragment.calendar.appointment.update

import android.os.Bundle
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.example.healthme.R
import com.example.healthme.databinding.FragmentUpdateAppointmentBinding

class UpdateAppointmentFragment : DialogFragment() {

    private var _binding: FragmentUpdateAppointmentBinding? = null
    private val binding: FragmentUpdateAppointmentBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpdateAppointmentBinding.inflate(inflater, container, false)

        binding.btnSave.setOnClickListener {

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
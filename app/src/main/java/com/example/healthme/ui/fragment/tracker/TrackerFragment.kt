package com.example.healthme.ui.fragment.tracker

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentTrackerBinding

class TrackerFragment : Fragment() {

    private var _binding: FragmentTrackerBinding? = null
    private val binding: FragmentTrackerBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.tracker)
        setHasOptionsMenu(true)
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)

        binding.btnAdd.setOnClickListener {
            binding.btnAdd.hide()
            binding.btnCancel.show()
            binding.btnSymptom.show()
            binding.btnMedicine.show()
            binding.blackoutLayout.visibility = View.VISIBLE
        }

        binding.btnCancel.setOnClickListener {
            hideDialogFAB()
        }

        binding.btnMedicine.setOnClickListener {
            hideDialogFAB()
            findNavController().navigate(R.id.to_addMedicineFragment)
        }

        binding.btnSymptom.setOnClickListener {
            hideDialogFAB()
            findNavController().navigate(R.id.to_addSymptomFragment)
        }

        return binding.root
    }

    private fun hideDialogFAB() {
        binding.btnAdd.show()
        binding.btnCancel.hide()
        binding.btnSymptom.hide()
        binding.btnMedicine.hide()
        binding.blackoutLayout.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.healthme.ui.fragment.calendar.settings.settings

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentCalendarSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*

class CalendarSettingsFragment : Fragment() {

    private var _binding: FragmentCalendarSettingsBinding? = null
    private val binding: FragmentCalendarSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.calendar_settings)
        _binding = FragmentCalendarSettingsBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).btm_nav.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        binding.toRecommendationBtn.setOnClickListener {
            findNavController().navigate(R.id.to_calendarRecommendedSettingsFragment)
        }

        binding.btnAddProcedure.setOnClickListener {
            findNavController().navigate(R.id.to_addProcedureFragment)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.to_calendarFragment)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
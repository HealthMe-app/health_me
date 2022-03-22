package com.example.healthme.ui.fragment.calendar.calendar

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.healthme.R
import com.example.healthme.databinding.FragmentTimelineBinding

class TimelineFragment : Fragment() {

    private var _binding: FragmentTimelineBinding? = null
    private val binding: FragmentTimelineBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.timeline)
        _binding = FragmentTimelineBinding.inflate(inflater, container, false)

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
package com.example.healthme.ui.fragment.calendar.calendar

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentCalendarBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val adapter = ListAdapter()

    private val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-M-d")
    private val formatDateServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.calendar)
        setHasOptionsMenu(true)
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        insertDateToQuery()

        binding.btnTimeline.setOnClickListener {
            findNavController().navigate(R.id.to_timelineFragment)
        }

        binding.btnAddAppointment.setOnClickListener {
            val action = CalendarFragmentDirections.toAddAppointmentFragment( "calendar")
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun insertDateToQuery() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        var currentDate = LocalDate.parse("$year-${month + 1}-$day", formatDate)
            .format(formatDateServer).toString()
        getAppointmentsToDate(currentDate)

        binding.calendarView.setOnDateChangeListener { _, mYear, mMonth, mDay ->
            currentDate = LocalDate.parse("$mYear-${mMonth + 1}-$mDay", formatDate)
                .format(formatDateServer).toString()
            getAppointmentsToDate(currentDate)
        }
    }

    private fun getAppointmentsToDate(currentDate: String) {
        viewModel.getAppointmentsToDate(currentDate)
        viewModel.myResponseAppointments.observe(this, Observer { response ->
            if (response.isSuccessful) {
                if (response.body()?.size != 0) {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.tvNoAppointment.visibility = View.GONE
                    response.body()?.let { adapter.setData(it) }
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.tvNoAppointment.visibility = View.VISIBLE
                }
            } else {
                Log.e("Response", response.errorBody()?.string()!!)
//                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            findNavController().navigate(R.id.to_calendarSettingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
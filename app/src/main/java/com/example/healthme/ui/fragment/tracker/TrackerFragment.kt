package com.example.healthme.ui.fragment.tracker

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.example.healthme.R
import com.example.healthme.databinding.FragmentTrackerBinding
import kotlinx.android.synthetic.main.fragment_tracker.*
import java.text.SimpleDateFormat
import java.util.*


class TrackerFragment : Fragment(){

    private val lastDayInCalendar = Calendar.getInstance(Locale.ENGLISH)
    private val sdf = SimpleDateFormat("MMMM yyyy", Locale.ENGLISH)
    private val cal = Calendar.getInstance(Locale.ENGLISH)

    // current date
    private val currentDate = Calendar.getInstance(Locale.ENGLISH)
    private val currentDay = currentDate[Calendar.DAY_OF_MONTH]
    private val currentMonth = currentDate[Calendar.MONTH]
    private val currentYear = currentDate[Calendar.YEAR]

    // selected date
    private var selectedDay: Int = currentDay
    private var selectedMonth: Int = currentMonth
    private var selectedYear: Int = currentYear

    // all days in month
    private val dates = ArrayList<Date>()

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

        lastDayInCalendar.add(Calendar.MONTH, 6)

        setUpCalendar()
        binding.previousMonth!!.setOnClickListener {
            if (cal.after(currentDate)) {
                cal.add(Calendar.MONTH, -1)
                if (cal == currentDate)
                    setUpCalendar()
                else
                    setUpCalendar(changeMonth = cal)
            }
        }
        binding.nextMonth!!.setOnClickListener {
            if (cal.before(lastDayInCalendar)) {
                cal.add(Calendar.MONTH, 1)
                setUpCalendar(changeMonth = cal)
            }
        }

        return binding.root
    }
    private fun setUpCalendar(changeMonth: Calendar? = null) {
        binding.monthYearText!!.text = sdf.format(cal.time)
        val monthCalendar = cal.clone() as Calendar
        val maxDaysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH)

        selectedDay =
            when {
                changeMonth != null -> changeMonth.getActualMinimum(Calendar.DAY_OF_MONTH)
                else -> currentDay
            }
        selectedMonth =
            when {
                changeMonth != null -> changeMonth[Calendar.MONTH]
                else -> currentMonth
            }
        selectedYear =
            when {
                changeMonth != null -> changeMonth[Calendar.YEAR]
                else -> currentYear
            }

        var currentPosition = 0
        dates.clear()
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1)

        while (dates.size < maxDaysInMonth) {
            // get position of selected day
            if (monthCalendar[Calendar.DAY_OF_MONTH] == selectedDay)
                currentPosition = dates.size
            dates.add(monthCalendar.time)
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        calendarRecyclerView!!.layoutManager = layoutManager
        val calendarAdapter = CalendarAdapter(requireContext(), dates, currentDate, changeMonth)
        calendarRecyclerView!!.adapter = calendarAdapter

        binding.recyclerView.adapter = calendarAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        when {
            currentPosition > 2 -> calendarRecyclerView!!.scrollToPosition(currentPosition - 3)
            maxDaysInMonth - currentPosition < 2 -> calendarRecyclerView!!.scrollToPosition(currentPosition)
            else -> calendarRecyclerView!!.scrollToPosition(currentPosition)
        }

        calendarAdapter.setOnItemClickListener(object : CalendarAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                val clickCalendar = Calendar.getInstance()
                clickCalendar.time = dates[position]
                selectedDay = clickCalendar[Calendar.DAY_OF_MONTH]
            }
        })
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

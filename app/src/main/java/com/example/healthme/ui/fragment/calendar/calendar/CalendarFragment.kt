package com.example.healthme.ui.fragment.calendar.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthme.R
import com.example.healthme.databinding.CalendarDayLayoutBinding
import com.example.healthme.databinding.FragmentCalendarBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.ui.activity.MainActivity
import com.example.healthme.ui.fragment.tracker.tracker.OnSwipeTouchListener
import com.example.healthme.util.Util
import com.example.healthme.util.Util.swipeToWeekMode
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.kizitonwose.calendarview.utils.next
import com.kizitonwose.calendarview.utils.previous
import com.kizitonwose.calendarview.utils.yearMonth
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*

class CalendarFragment : Fragment() {

    private var _binding: FragmentCalendarBinding? = null
    private val binding: FragmentCalendarBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val adapter = ListAdapter()

    private val today = LocalDate.now()
    private var selectedDate: LocalDate = today
    private var tagDatesList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as MainActivity).btm_nav.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
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

        createCalendar()
        getAppointments()
        getAppointmentsToDate(today.toString())

        binding.btnTimeline.setOnClickListener {
            findNavController().navigate(R.id.to_timelineFragment)
        }

        binding.btnAddAppointment.setOnClickListener {
            val action = CalendarFragmentDirections.toAddAppointmentFragment( "calendar")
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun getAppointments() {
        tagDatesList.clear()
        viewModel.getAppointments()
        viewModel.myResponseAppointments.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.forEach { appointment ->
                    val tagDate = appointment.date_time.substring(0, 10)
                    tagDatesList.add(tagDate)
                    createCalendar()
                }
            } else {
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getAppointmentsToDate(currentDate: String) {
        viewModel.getAppointmentsToDate(currentDate)
        viewModel.myResponseAppointmentsToDate.observe(this, Observer { response ->
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
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    fun createCalendar() {
        val currentMonth = YearMonth.now()
        val firstMonth = currentMonth.minusMonths(10)
        val lastMonth = currentMonth.plusMonths(10)
        val firstDayOfWeek = DayOfWeek.MONDAY
        binding.calendarView.setup(firstMonth, lastMonth, firstDayOfWeek)
        binding.calendarView.scrollToDate(selectedDate)

        class DayViewContainer(view: View) : ViewContainer(view) {
            lateinit var day: CalendarDay
            val dayLayout = CalendarDayLayoutBinding.bind(view).dayLayout
            val textView = CalendarDayLayoutBinding.bind(view).dayText
            val dotView = CalendarDayLayoutBinding.bind(view).dotView

            init {
                textView.setOnClickListener {
                    if (day.owner == DayOwner.THIS_MONTH) {
                        if (selectedDate != day.date) {
                            val oldDate = selectedDate
                            selectedDate = day.date
                            binding.calendarView.notifyDateChanged(day.date)
                            oldDate.let { binding.calendarView.notifyDateChanged(oldDate) }
                        }
                    }
                    getAppointmentsToDate(selectedDate.toString())
                }
            }
        }

        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.textView
                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.setTextColor(resources.getColor(R.color.dark_birch))

                    when (day.date) {
                        selectedDate -> {
                            textView.setTextColor(resources.getColor(R.color.white))
                            container.dayLayout.setBackgroundResource(R.drawable.bg_dark_pink_10)
                            container.dotView.visibility = View.INVISIBLE
                        }
                        today -> {
                            textView.setTextColor(resources.getColor(R.color.dark_pink))
                            container.dayLayout.background = null
                            container.dotView.visibility =
                                if (day.date.toString() in tagDatesList) View.VISIBLE
                                else View.INVISIBLE
                        }
                        else -> {
                            textView.setTextColor(resources.getColor(R.color.dark_birch))
                            container.dayLayout.background = null
                            container.dotView.visibility =
                                if (day.date.toString() in tagDatesList) View.VISIBLE
                                else View.INVISIBLE
                        }
                    }
                } else {
                    textView.setTextColor(resources.getColor(R.color.gray))
                    container.dayLayout.background = null
                    container.dotView.visibility = View.INVISIBLE
                }
            }
        }

        binding.calendarView.monthScrollListener = {
            binding.currentMonth.text = resources.getStringArray(R.array.month)[it.month - 1]
            binding.currentYear.text = it.year.toString()

            if (binding.calendarView.maxRowCount == 6) {
                binding.currentMonth.text = resources.getStringArray(R.array.month)[it.month - 1]
                binding.currentYear.text = it.yearMonth.year.toString()
            } else {
                val firstDate = it.weekDays.first().first().date
                val lastDate = it.weekDays.last().last().date
                if (firstDate.yearMonth == lastDate.yearMonth) {
                    binding.currentMonth.text =
                        resources.getStringArray(R.array.month)[firstDate.monthValue - 1]
                    binding.currentYear.text = firstDate.yearMonth.year.toString()
                } else {
                    binding.currentMonth.text =
                        "${resources.getStringArray(R.array.month)[firstDate.monthValue - 1]} - ${
                            resources.getStringArray(R.array.month)[lastDate.monthValue - 1]
                        }"
                    if (firstDate.year == lastDate.year) {
                        binding.currentYear.text = firstDate.yearMonth.year.toString()
                    } else {
                        binding.currentYear.text =
                            "${firstDate.yearMonth.year} - ${lastDate.yearMonth.year}"
                    }
                }
            }
        }

        binding.calLayout.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                if (binding.calendarView.maxRowCount == 6)
                    swipeToWeekMode(binding.calendarView, true, selectedDate)
            }

            override fun onSwipeBottom() {
                if (binding.calendarView.maxRowCount == 1) {
                    binding.currentMonth.text =
                        resources.getStringArray(R.array.month)[selectedDate.monthValue - 1]
                    swipeToWeekMode(binding.calendarView, false, selectedDate)
                    binding.currentMonth.layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
                }
            }
        })

        binding.arrowRight.setOnClickListener {
            if (binding.calendarView.maxRowCount == 1) {
                binding.calendarView.smoothScrollToDate(
                    binding.calendarView.findLastVisibleDay()?.date!!.plusDays(
                        1
                    )
                )
            } else {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                }
            }
        }

        binding.arrowLeft.setOnClickListener {
            if (binding.calendarView.maxRowCount == 1) {
                binding.calendarView.smoothScrollToDate(
                    binding.calendarView.findFirstVisibleDay()?.date!!.minusDays(
                        1
                    )
                )
            } else {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                }
            }
        }
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
        tagDatesList.clear()
        _binding = null
    }
}
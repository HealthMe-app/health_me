package com.example.healthme.ui.fragment.tracker.tracker

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthme.R
import com.example.healthme.databinding.CalendarDayLayoutBinding
import com.example.healthme.databinding.FragmentTrackerBinding
import com.example.healthme.repository.ApiRepository
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
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth

class TrackerFragment : Fragment() {

    private var _binding: FragmentTrackerBinding? = null
    private val binding: FragmentTrackerBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private val adapter = ListAdapter()

    private val today = LocalDate.now()
    private var selectedDate: LocalDate = today
    private var tagDatesList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.tracker)
        setHasOptionsMenu(true)
        _binding = FragmentTrackerBinding.inflate(inflater, container, false)
        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        getNotes()
        getNotesToDate(today.toString())

        binding.btnAdd.setOnClickListener {
            binding.btnAdd.hide()
            binding.btnCancel.show()
            binding.btnSymptom.show()
            binding.btnMedicine.show()
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
            val action = TrackerFragmentDirections.toAddSymptomFragment("tracker")
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun hideDialogFAB() {
        binding.btnAdd.show()
        binding.btnCancel.hide()
        binding.btnSymptom.hide()
        binding.btnMedicine.hide()
    }

    private fun getNotes() {
        tagDatesList.clear()
        viewModel.getNotes()
        viewModel.myResponseNotes.observe(this, Observer { response ->
            if (response.isSuccessful) {
                response.body()?.forEach { note ->
                    val tagDate = note.date_time.substring(0, 10)
                    tagDatesList.add(tagDate)
                    createCalendar()
                }
            } else {
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getNotesToDate(currentDate: String) {
        viewModel.getNotesToDate(currentDate)
        viewModel.myResponseNotesToDate.observe(this, Observer { response ->
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
        binding.calendarView.updateMonthConfiguration(
            inDateStyle = InDateStyle.FIRST_MONTH,
            maxRowCount = 1,
            hasBoundaries = false
        )
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
                    getNotesToDate(selectedDate.toString())
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

        binding.trackerLayout.setOnTouchListener(object : OnSwipeTouchListener(requireContext()) {
            override fun onSwipeTop() {
                swipeToWeekMode(true)
            }

            override fun onSwipeBottom() {
                binding.currentMonth.text =
                    resources.getStringArray(R.array.month)[selectedDate.monthValue - 1]
                swipeToWeekMode(false)
                binding.currentMonth.layoutParams.width = ConstraintLayout.LayoutParams.WRAP_CONTENT
            }
        })

        binding.arrowRight.setOnClickListener {
            if (binding.calendarView.maxRowCount == 1) {
                binding.calendarView.smoothScrollToDate(binding.calendarView.findLastVisibleDay()?.date!!.plusDays(1))
            } else {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.next)
                }
            }
        }

        binding.arrowLeft.setOnClickListener {
            if (binding.calendarView.maxRowCount == 1) {
                binding.calendarView.smoothScrollToDate(binding.calendarView.findFirstVisibleDay()?.date!!.minusDays(1))
            } else {
                binding.calendarView.findFirstVisibleMonth()?.let {
                    binding.calendarView.smoothScrollToMonth(it.yearMonth.previous)
                }
            }
        }
    }

    fun swipeToWeekMode(monthToWeek: Boolean) {
        val firstDate = binding.calendarView.findFirstVisibleDay()?.date
        val lastDate = binding.calendarView.findLastVisibleDay()?.date

        val oneWeekHeight = binding.calendarView.daySize.height
        val oneMonthHeight = oneWeekHeight * 6

        val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
        val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

        val animator = ValueAnimator.ofInt(oldHeight, newHeight)
        animator.addUpdateListener { animator ->
            binding.calendarView.updateLayoutParams {
                height = animator.animatedValue as Int
            }
        }

        animator.doOnStart {
            if (!monthToWeek) {
                binding.calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    maxRowCount = 6,
                    hasBoundaries = true
                )
            }
        }
        animator.doOnEnd {
            if (monthToWeek) {
                binding.calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.FIRST_MONTH,
                    maxRowCount = 1,
                    hasBoundaries = false
                )
            }

            if (monthToWeek) {
                binding.calendarView.scrollToDate(selectedDate)
            } else {
                if (firstDate!!.yearMonth == lastDate!!.yearMonth) {
                    binding.calendarView.scrollToMonth(firstDate.yearMonth)
                } else {
                    binding.calendarView.scrollToMonth(
                        minOf(
                            selectedDate.yearMonth,
                            YearMonth.now().plusMonths(10)
                        )
                    )
                }
            }
        }
        animator.duration = 250
        animator.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tagDatesList.clear()
        _binding = null
    }
}
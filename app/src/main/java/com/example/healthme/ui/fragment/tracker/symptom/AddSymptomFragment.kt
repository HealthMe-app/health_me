package com.example.healthme.ui.fragment.tracker.symptom

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.view.*
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentAddSymptomBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddSymptomFragment : DialogFragment() {

    private var _binding: FragmentAddSymptomBinding? = null
    private val binding: FragmentAddSymptomBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val args by navArgs<AddSymptomFragmentArgs>()
    private val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    private val formatDateUser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val formatDateServer: DateTimeFormatter =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val formatDateTime: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    var ntype: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSymptomBinding.inflate(inflater, container, false)
        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]
        setSeverity()
        binding.trackerDateTime.setHintTextColor(resources.getColor(R.color.dark_birch))

        binding.dateTimeLayout.setEndIconOnClickListener {
            setDate()
        }
        binding.trackerDateTime.setOnClickListener {
            setDate()
        }

        binding.btnSave.setOnClickListener {
            insertDataToQuery()
        }

        binding.btnCancel.setOnClickListener {
            dialog?.cancel()
        }

        return binding.root
    }

    private fun setSeverity() {
        binding.mildSeverity.setOnClickListener {
            ntype = 1
            binding.mildSeverity.compatElevation = 0f
            binding.mildSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.dark_birch))
            binding.averageSeverity.compatElevation = 30f
            binding.averageSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.orange))
            binding.severeSeverity.compatElevation = 30f
            binding.severeSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.middle_pink))
        }

        binding.averageSeverity.setOnClickListener {
            ntype = 2
            binding.averageSeverity.compatElevation = 0f
            binding.averageSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.dark_orange))
            binding.mildSeverity.compatElevation = 30f
            binding.mildSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.birch))
            binding.severeSeverity.compatElevation = 30f
            binding.severeSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.middle_pink))
        }

        binding.severeSeverity.setOnClickListener {
            ntype = 3
            binding.severeSeverity.compatElevation = 0f
            binding.severeSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.dark_pink))
            binding.averageSeverity.compatElevation = 30f
            binding.averageSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.orange))
            binding.mildSeverity.compatElevation = 30f
            binding.mildSeverity.backgroundTintList =
                ColorStateList.valueOf(resources.getColor(R.color.birch))
        }
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(), R.style.DialogTheme,
            TimePickerDialog.OnTimeSetListener { _, mHour, mMinute ->
                val time =
                    (if (mHour < 10) "0$mHour" else mHour.toString()) + ":" + (if (mMinute < 10) "0$mMinute" else mMinute.toString())
                binding.trackerDateTime.setText(
                    binding.trackerDateTime.text.toString().plus(" $time")
                )
            }, hour, minute,
            DateFormat.is24HourFormat(requireContext())
        )
        timePickerDialog.show()
        timePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.dark_pink))
        timePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.birch))

        val datePickerDialog = DatePickerDialog(
            requireContext(), R.style.DialogTheme,
            DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                val date = LocalDate.parse("$mDay.${mMonth + 1}.$mYear", formatDate)
                    .format(formatDateUser).toString()
                binding.trackerDateTime.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.dark_pink))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.birch))
    }

    private fun insertDataToQuery() {
        val name = binding.trackerName.text.toString()
        var dateTime = binding.trackerDateTime.text.toString()
        val comment = binding.trackerComment.text.toString()

        if (inputCheck(name, dateTime)) {
            dateTime = LocalDateTime.parse(binding.trackerDateTime.text, formatDateTime)
                .format(formatDateServer).toString()
            addNote(name, dateTime, comment)
        } else {
            Toast.makeText(
                requireContext(), "?????????????????? ????????: ?????????????? ??????????????, ????????????????, ????????",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun inputCheck(name: String, date: String): Boolean {
        return !(TextUtils.isEmpty(name) || date.length != 16 || (date == resources.getString(R.string.date_and_time)) || ntype == 0)
    }

    private fun addNote(name: String, dateTime: String, comment: String) {
        viewModel.addNote(ntype, name, dateTime, comment)
        viewModel.myResponseNote.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "???????????? ?????????????????? ??????????????", Toast.LENGTH_LONG)
                    .show()
                dialog?.cancel()
                if (args.parentFragment == "home") findNavController().navigate(R.id.to_homeFragment)
                else findNavController().navigate(R.id.to_trackerFragment)
            } else {
                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
            }
        })
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
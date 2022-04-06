package com.example.healthme.ui.fragment.calendar.appointment.add

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.preference.PreferenceManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentAddAppointmentBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.ui.fragment.calendar.appointment.update.UpdateAppointmentFragmentArgs
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import com.google.android.material.timepicker.TimeFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddAppointmentFragment : DialogFragment() {

    private var _binding: FragmentAddAppointmentBinding? = null
    private val binding: FragmentAddAppointmentBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    private val args by navArgs<AddAppointmentFragmentArgs>()
    private val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    private val formatDateUser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val formatDateServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    private val formatDateTime: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddAppointmentBinding.inflate(inflater, container, false)
        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]
        binding.appointmentDateTime.setHintTextColor(resources.getColor(R.color.dark_birch))

        binding.dateTimeLayout.setEndIconOnClickListener {
            setDate()
        }
        binding.appointmentDateTime.setOnClickListener {
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
                binding.appointmentDateTime.setText(
                    binding.appointmentDateTime.text.toString().plus(" $time")
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
                binding.appointmentDateTime.setText(date)
            }, year, month, day
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
            .setTextColor(resources.getColor(R.color.dark_pink))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
            .setTextColor(resources.getColor(R.color.birch))
    }

    private fun insertDataToQuery() {
        val type = binding.appointmentType.text.toString()
        val name = binding.appointmentName.text.toString()
        var dateTime = binding.appointmentDateTime.text.toString()
        val address = binding.appointmentAddress.text.toString()
        val comment = binding.appointmentComment.text.toString()

        // 1 - консультация, 2 - обследование, 3 - прививка
        if (inputCheck(type, name, dateTime, address)) {
            val ptype = resources.getStringArray(R.array.appointmentTypes).indexOf(type) + 1
            dateTime = LocalDateTime.parse(binding.appointmentDateTime.text, formatDateTime).format(formatDateServer).toString()
            addAppointment(name, address, dateTime, comment, ptype)
        } else {
            Toast.makeText(
                requireContext(), "Заполните поля: тип, название, дата, адрес",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun inputCheck(type: String, name: String, date: String, address: String): Boolean {
        return !((type == resources.getString(R.string.type)) || TextUtils.isEmpty(name) || date.length != 16
                || (date == resources.getString(R.string.date_and_time)) || TextUtils.isEmpty(address))
    }

    private fun addAppointment(
        name: String,
        address: String,
        dateTime: String,
        comment: String,
        ptype: Int
    ) {
        viewModel.addAppointment(name, address, dateTime, comment, ptype)
        viewModel.myResponseAppointment.observe(this, Observer { response ->
            if (response.isSuccessful) {
                Toast.makeText(requireContext(), "Запись добавлена успешно", Toast.LENGTH_LONG)
                    .show()
                dialog?.cancel()
                if (args.parentFragment == "home") findNavController().navigate(R.id.to_homeFragment)
                else findNavController().navigate(R.id.to_calendarFragment)
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

        val appointmentTypes = resources.getStringArray(R.array.appointmentTypes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, appointmentTypes)
        binding.appointmentType.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
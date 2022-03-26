package com.example.healthme.ui.fragment.registration

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentRegistrationBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding: FragmentRegistrationBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var dateToServer: String

    private val formatDate: DateTimeFormatter = DateTimeFormatter.ofPattern("d.M.yyyy")
    private val formatUser: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    private val formatServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()

        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]

        val gender = resources.getStringArray(R.array.gender)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, gender)
        binding.registrationGender.setAdapter(arrayAdapter)
        setDate()

        binding.toAuthorizationBtn.setOnClickListener {
            findNavController().navigate(R.id.to_loginFragment)
        }

        binding.btnRegistration.setOnClickListener {
            register()
        }

        binding.registrationGender.setOnClickListener {
            hideKeyboard()
        }

        return binding.root
    }

    private fun setDate() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        binding.registrationDateOfBirth.setOnClickListener {
            val datePickerDialog = DatePickerDialog(
                requireContext(), R.style.DialogTheme,
                DatePickerDialog.OnDateSetListener { _, mYear, mMonth, mDay ->
                    val date = LocalDate.parse("$mDay.$mMonth.$mYear", formatDate)
                        .format(formatUser).toString()
                    dateToServer = LocalDate.parse("$mDay.$mMonth.$mYear", formatDate)
                        .format(formatServer).toString()
                    binding.registrationDateOfBirth.setText(date)
                }, year, month, day
            )
            datePickerDialog.show()
            datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE)
                .setTextColor(resources.getColor(R.color.dark_pink))
            datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE)
                .setTextColor(resources.getColor(R.color.birch))
        }
    }

    private fun register() {
        val name = binding.registrationName.text.toString()
        val email = binding.registrationEmail.text.toString()
        val password = binding.registrationPassword.text.toString()
        // male - 1, female - 2
        val gender =
            if (binding.registrationGender.text.toString() == resources.getStringArray(R.array.gender)[1])
                '2'
            else
                '1'

        val date = dateToServer

        if (inputCheck(name, email, password, date)) {
            viewModel.register(email, name, gender, date, password)
            viewModel.myResponseUserInfo.observe(requireActivity()) { response ->
                if (response.isSuccessful) {
                    Toast.makeText(
                        requireContext(), "Регистрация прошла успешно",
                        Toast.LENGTH_LONG
                    ).show()
                    findNavController().navigate(R.id.to_homeFragment)
                } else {
                    val errorText =
                        response.errorBody()!!.string().substringAfter("[\"").dropLast(3)
                    Toast.makeText(requireContext(), errorText, Toast.LENGTH_LONG).show()
                }
            }
        } else {
            Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(
        name: String,
        email: String,
        password: String,
        date: String
    ): Boolean {
        return !(TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(date) || binding.registrationGender.text.isEmpty())
    }

    private fun hideKeyboard() {
        val inputMethodManager =
            ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
        if (inputMethodManager!!.isActive)
            inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
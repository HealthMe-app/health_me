package com.example.healthme.ui.fragment.home.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentHomeBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.ui.activity.MainActivity
import com.example.healthme.viewmodel.MainViewModel
import com.example.healthme.viewmodel.MainViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import java.time.Period
import kotlin.random.Random

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
        (activity as MainActivity).btm_nav.visibility = View.VISIBLE
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.home)
        setHasOptionsMenu(true)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val repository = ApiRepository()
        val viewModelFactory = MainViewModelFactory(
            PreferenceManager.getDefaultSharedPreferences(requireActivity()),
            repository
        )
        viewModel = ViewModelProvider(activity!!, viewModelFactory)[MainViewModel::class.java]
        checkAuthorizationUser()

        val reminders = resources.getStringArray(R.array.reminders)
        binding.txtReminder.text = reminders[Random.nextInt(reminders.size)]

        binding.btnAddAppointment.setOnClickListener {
            findNavController().navigate(R.id.to_addAppointmentFragment)
        }

        binding.btnAddSymptom.setOnClickListener {
            findNavController().navigate(R.id.to_addSymptomFragment)
        }

        return binding.root
    }

    private fun checkAuthorizationUser() {
        if (viewModel.getUser()) {
            viewModel.myResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    binding.tvName.text = response.body()?.first_name
                    binding.tvGender.text = response.body()?.sex.toString()
                    binding.tvAge.text = response.body()?.date_of_birth?.let { getAge(it) }
                } else {
                    val errorText =
                        response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                    Log.e("Error Response", errorText.toString())
                    findNavController().navigate(R.id.to_welcomeFragment)
                }
            })
        } else findNavController().navigate(R.id.to_welcomeFragment)
    }

    private fun getAge(date: String): String {
        val yearMonthDay = date.split("-")
        return Period.between(
            LocalDate.of(yearMonthDay[0].toInt(), yearMonthDay[1].toInt(), yearMonthDay[2].toInt()),
            LocalDate.now()
        ).years.toString()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.settings_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_settings) {
            findNavController().navigate(R.id.to_profileSettingsFragment)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
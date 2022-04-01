package com.example.healthme.ui.fragment.home.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthme.R
import com.example.healthme.databinding.FragmentHomeBinding
import com.example.healthme.repository.ApiRepository
import com.example.healthme.ui.activity.MainActivity
import com.example.healthme.ui.fragment.calendar.calendar.CalendarFragmentDirections
import com.example.healthme.ui.fragment.tracker.tracker.TrackerFragmentDirections
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
    private val adapter = ListAdapter()

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

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        getClosetAppointments()

        binding.btnAddAppointment.setOnClickListener {
            val action = CalendarFragmentDirections.toAddAppointmentFragment( "home")
            findNavController().navigate(action)
        }

        binding.btnAddSymptom.setOnClickListener {
            val action = TrackerFragmentDirections.toAddSymptomFragment( "home")
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun checkAuthorizationUser() {
        if (viewModel.getUser()) {
            viewModel.myResponse.observe(this, Observer { response ->
                if (response.isSuccessful) {
                    binding.tvName.text = response.body()?.first_name
                    binding.tvGender.text = getString(R.string.gen).plus(" ").plus(
                        if (response.body()?.sex == '1') "мужской"
                        else "женский"
                    )
                    response.body()?.sex.toString()
                    binding.tvAge.text =
                        getString(R.string.age, getAge(response.body()!!.date_of_birth))

//                    fetchSvg(requireContext(), response.body()!!.pic, binding.profilePic)
                } else {
                    val errorText =
                        response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                    Log.e("Error Response", errorText.toString())
                    findNavController().navigate(R.id.to_welcomeFragment)
                }
            })
        } else findNavController().navigate(R.id.to_welcomeFragment)
    }

    private fun getAge(date: String): Int {
        val yearMonthDay = date.split("-")
        return Period.between(
            LocalDate.of(yearMonthDay[0].toInt(), yearMonthDay[1].toInt(), yearMonthDay[2].toInt()),
            LocalDate.now()
        ).years
    }

    private fun getClosetAppointments() {
        viewModel.getClosetAppointments()
        viewModel.myResponseClosetAppointments.observe(this, Observer { response ->
            if (response.isSuccessful) {
                if (response.body()?.size != 0) {
                    binding.recyclerView.visibility = View.VISIBLE
                    binding.nullClosestAppointments.visibility = View.GONE
                    response.body()?.let { adapter.setData(it) }
                } else {
                    binding.recyclerView.visibility = View.GONE
                    binding.nullClosestAppointments.visibility = View.VISIBLE
                }
            } else {
                Log.e("Response", response.errorBody()?.string()!!)
//                val errorText = response.errorBody()?.string()?.substringAfter(":\"")?.dropLast(3)
                Toast.makeText(requireContext(), response.errorBody()?.string(), Toast.LENGTH_LONG)
                    .show()
            }
        })
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
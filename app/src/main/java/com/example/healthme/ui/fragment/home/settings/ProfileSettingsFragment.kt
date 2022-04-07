package com.example.healthme.ui.fragment.home.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.healthme.R
import com.example.healthme.databinding.FragmentProfileSettingsBinding
import kotlinx.android.synthetic.main.activity_main.*

class ProfileSettingsFragment : Fragment() {

    private var _binding: FragmentProfileSettingsBinding? = null
    private val binding: FragmentProfileSettingsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title =
            resources.getString(R.string.settings)
        _binding = FragmentProfileSettingsBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).btm_nav.visibility = View.GONE
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        binding.btnEditAvatar.setOnClickListener {
            findNavController().navigate(R.id.to_changeAvatarFragment)
        }

        binding.frameProfile.setOnClickListener {
            findNavController().navigate(R.id.to_changeProfileFragment)
        }

        binding.frameSecurity.setOnClickListener {
            findNavController().navigate(R.id.to_securitySettingsFragment)
        }

        binding.frameNotification.setOnClickListener {
            findNavController().navigate(R.id.to_notificationSettingsFragment)
        }

        binding.frameExit.setOnClickListener {
            findNavController().navigate(R.id.to_logoutFragment)
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        findNavController().navigate(R.id.to_homeFragment)
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.healthme.ui.fragment.dict

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.healthme.R
import com.example.healthme.databinding.FragmentDictBinding

class DictFragment : Fragment() {

    private var _binding: FragmentDictBinding? = null
    private val binding: FragmentDictBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.dict)
        _binding = FragmentDictBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
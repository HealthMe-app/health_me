package com.example.healthme.ui.fragment.dict

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.healthme.R
import com.example.healthme.ui.viewmodel.MedicineViewModel
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import com.example.healthme.ui.util.Util.hideContent
import com.example.healthme.ui.util.Util.showContent
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.addTextChangedListener
import com.example.healthme.databinding.FragmentDictBinding

class DictFragment : Fragment() {

    private var _binding: FragmentDictBinding? = null
    private val binding: FragmentDictBinding get() = _binding!!
    lateinit var mMedicineViewModel: MedicineViewModel
    private val adapter = ListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.dict)
        _binding = FragmentDictBinding.inflate(inflater, container, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        mMedicineViewModel = ViewModelProvider(this)[MedicineViewModel::class.java]
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        search()
    }

    private fun search() {
        binding.searchEt.text = null
        var search = true
        val inputMethodManager =
            getSystemService(requireContext(), InputMethodManager::class.java)

        binding.searchEt.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus)
                hideContent(binding)
            else {
                binding.searchEt.clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
            }
        }

        binding.searchBtn.setOnClickListener {
            search = if (search) {
                clickToSearch()
                false
            } else {
                binding.searchEt.clearFocus()
                inputMethodManager?.hideSoftInputFromWindow(binding.root.windowToken, 0)
                showContent(binding)
                true
            }
        }

        binding.searchEt.addTextChangedListener {
            searchDatabase(it.toString())
        }
    }

    private fun clickToSearch() {
        binding.searchEt.requestFocus()
        hideContent(binding)
        val inputMethodManager =
            getSystemService(requireContext(), InputMethodManager::class.java)
        inputMethodManager?.showSoftInput(binding.searchEt, 0)
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        if (query.isNotEmpty()) {
            binding.recyclerView.visibility = View.VISIBLE
            mMedicineViewModel.searchDatabase(searchQuery).observe(this, { list ->
                list.let {
                    if (it.isEmpty())
                        binding.notFound.visibility = View.VISIBLE
                    else
                        binding.notFound.visibility = View.INVISIBLE
                    adapter.setData(it)
                }
            })
        } else binding.recyclerView.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.searchEt.text = null
        _binding = null
    }
}
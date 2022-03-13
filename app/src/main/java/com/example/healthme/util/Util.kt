package com.example.healthme.util

import android.view.View
import com.example.healthme.R
import com.example.healthme.databinding.FragmentDictBinding

object Util {
    // for dict fragment
    @JvmStatic
    fun hideContent(binding: FragmentDictBinding) {
        binding.searchBtn.setImageResource(R.drawable.ic_search_close)
        binding.imageDict.visibility = View.GONE
        binding.textDict.visibility = View.GONE
        binding.layoutDict.requestLayout()
    }

    @JvmStatic
    fun showContent(binding: FragmentDictBinding) {
        binding.searchBtn.setImageResource(R.drawable.ic_search)
        binding.imageDict.visibility = View.VISIBLE
        binding.textDict.visibility = View.VISIBLE
        binding.notFound.visibility = View.GONE
        binding.searchEt.text = null
        binding.layoutDict.requestLayout()
    }
}
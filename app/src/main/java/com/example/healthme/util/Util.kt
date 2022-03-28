package com.example.healthme.util

import android.view.View
import com.example.healthme.databinding.FragmentDictBinding
import okhttp3.OkHttpClient
import android.content.Context
import android.widget.ImageView
import com.example.healthme.R
import java.io.IOException
import java.io.InputStream
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import com.pixplicity.sharp.Sharp

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

    private var httpClient: OkHttpClient? = null

    @JvmStatic
    fun fetchSvg(context: Context, url: String, target: ImageView) {
        if (httpClient == null) {
            httpClient = OkHttpClient.Builder()
                .cache(Cache(context.cacheDir, 5 * 1024 * 1014))
                .build()
        }

        val request: Request = Request.Builder().url(url).build()
        httpClient!!.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                target.setImageResource(R.drawable.ic_cross)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val stream: InputStream = response.body?.byteStream()!!
                Sharp.loadInputStream(stream).into(target)
                stream.close()
            }
        })
    }
}
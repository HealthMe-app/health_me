package com.example.healthme.util

import android.animation.ValueAnimator
import android.view.View
import com.example.healthme.databinding.FragmentDictBinding
import okhttp3.OkHttpClient
import android.content.Context
import android.widget.ImageView
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.updateLayoutParams
import com.example.healthme.R
import com.kizitonwose.calendarview.CalendarView
import com.kizitonwose.calendarview.model.InDateStyle
import com.kizitonwose.calendarview.utils.yearMonth
import java.io.IOException
import java.io.InputStream
import okhttp3.Cache
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import com.pixplicity.sharp.Sharp
import java.time.LocalDate
import java.time.YearMonth

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

    // load svg and put into imageview
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

    // change calendar mode by swipe
    @JvmStatic
    fun swipeToWeekMode(calendarView: CalendarView, monthToWeek: Boolean, selectedDate: LocalDate) {
        val firstDate = calendarView.findFirstVisibleDay()?.date
        val lastDate = calendarView.findLastVisibleDay()?.date

        val oneWeekHeight = calendarView.daySize.height
        val oneMonthHeight = oneWeekHeight * 6

        val oldHeight = if (monthToWeek) oneMonthHeight else oneWeekHeight
        val newHeight = if (monthToWeek) oneWeekHeight else oneMonthHeight

        val animator = ValueAnimator.ofInt(oldHeight, newHeight)
        animator.addUpdateListener { animator ->
            calendarView.updateLayoutParams {
                height = animator.animatedValue as Int
            }
        }

        animator.doOnStart {
            if (!monthToWeek) {
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.ALL_MONTHS,
                    maxRowCount = 6,
                    hasBoundaries = true
                )
            }
        }
        animator.doOnEnd {
            if (monthToWeek) {
                calendarView.updateMonthConfiguration(
                    inDateStyle = InDateStyle.FIRST_MONTH,
                    maxRowCount = 1,
                    hasBoundaries = false
                )
            }
            if (monthToWeek) {
                calendarView.scrollToDate(selectedDate)
            } else {
                if (firstDate!!.yearMonth == lastDate!!.yearMonth) {
                    calendarView.scrollToMonth(firstDate.yearMonth)
                } else {
                    calendarView.scrollToMonth(
                        minOf(
                            selectedDate.yearMonth,
                            YearMonth.now().plusMonths(10)
                        )
                    )
                }
            }
        }
        animator.duration = 250
        animator.start()
    }
}
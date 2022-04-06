package com.example.healthme.ui.fragment.home.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.healthme.R
import com.example.healthme.model.Appointment
import kotlinx.android.synthetic.main.row_closet_appointment.view.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var appointmentList = emptyList<Appointment>()

    private val formatDateServer: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_closet_appointment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = appointmentList[position]
        holder.itemView.name.text = currentItem.name
        val dateTime = LocalDate.parse(currentItem.date_time, formatDateServer)
        holder.itemView.dayOfMonth.text = dateTime.dayOfMonth.toString()
        holder.itemView.month.text = holder.itemView.resources.getStringArray(R.array.month)[dateTime.monthValue - 1]
        var dayOfWeekTime = holder.itemView.resources.getStringArray(R.array.dayOfWeek)[dateTime.dayOfWeek.value - 1]
        dayOfWeekTime += ", ${currentItem.date_time.substringAfter('T').substringBeforeLast(':')}"
        holder.itemView.dayOfWeekTime.text = dayOfWeekTime

        holder.itemView.rowLayout.setOnClickListener {
            val action = HomeFragmentDirections.toUpdateAppointmentFragment(currentItem.id, "home")
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return appointmentList.size
    }

    fun setData(appointment: List<Appointment>) {
        this.appointmentList = appointment
        notifyDataSetChanged()
    }
}
package com.example.healthme.ui.fragment.calendar.calendar

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.healthme.R
import com.example.healthme.model.Appointment
import com.example.healthme.util.Constants.Companion.BASE_URL
import com.example.healthme.util.Util.fetchSvg
import kotlinx.android.synthetic.main.row_appointment.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var appointmentList = emptyList<Appointment>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_appointment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = appointmentList[position]
        holder.itemView.name.text = currentItem.name
        holder.itemView.date.text = currentItem.date_time.substringAfter('T').substringBeforeLast(':')

        val context: Context = holder.itemView.context
        fetchSvg(context, BASE_URL + currentItem.ptype.icon_pink, holder.itemView.imageType)

        holder.itemView.rowLayout.setOnClickListener {
            val action = CalendarFragmentDirections.toUpdateAppointmentFragment(currentItem.id, "calendar")
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
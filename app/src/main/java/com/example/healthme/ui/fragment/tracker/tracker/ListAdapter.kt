package com.example.healthme.ui.fragment.tracker.tracker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.healthme.R
import com.example.healthme.model.Note
import com.example.healthme.util.Constants.Companion.BASE_URL
import com.example.healthme.util.Util.fetchSvg
import kotlinx.android.synthetic.main.row_appointment.view.*
import kotlinx.android.synthetic.main.row_appointment.view.imageType
import kotlinx.android.synthetic.main.row_appointment.view.name
import kotlinx.android.synthetic.main.row_note.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var noteList = emptyList<Note>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.row_note, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = noteList[position]
        holder.itemView.name.text = currentItem.name
        holder.itemView.time.text = currentItem.date_time.substringAfter('T').substringBeforeLast(':')

        val context: Context = holder.itemView.context
        fetchSvg(context, BASE_URL + currentItem.ntype.icon_birch, holder.itemView.imageType)

        holder.itemView.rowSymptom.setOnClickListener {
            if (currentItem.ntype.id == 4) {
                val action = TrackerFragmentDirections.toUpdateMedicineFragment(currentItem.id)
                holder.itemView.findNavController().navigate(action)
            } else {
                val action = TrackerFragmentDirections.toUpdateSymptomFragment(currentItem.id)
                holder.itemView.findNavController().navigate(action)
            }
        }
    }

    override fun getItemCount(): Int {
        return noteList.size
    }

    fun setData(note: List<Note>) {
        this.noteList = note
        notifyDataSetChanged()
    }
}
package com.example.healthme.ui.fragment.dict

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.example.healthme.R
import com.example.healthme.model.Medicine
import kotlinx.android.synthetic.main.medicine_recycler_row.view.*

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var medicineList = emptyList<Medicine>()

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.medicine_recycler_row, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = medicineList[position]
        holder.itemView.medName.text = currentItem.name

        holder.itemView.medName_frame.setOnClickListener {
            var visible = if (holder.itemView.medIndication_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medIndication_frame.visibility = visible
            visible = if (holder.itemView.medContraindication_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medContraindication_frame.visibility = visible
            visible = if (holder.itemView.medSideEffects_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medSideEffects_frame.visibility = visible
            visible = if (holder.itemView.medDosage_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medDosage_frame.visibility = visible
            if (visible == View.VISIBLE) holder.itemView.mainArrow.setImageResource(R.drawable.ic_arrow_up)
            else holder.itemView.mainArrow.setImageResource(R.drawable.ic_arrow)

            holder.itemView.medIndication_txt_frame.visibility = View.GONE
            holder.itemView.medContraindication_txt_frame.visibility = View.GONE
            holder.itemView.medSideEffects_txt_frame.visibility = View.GONE
            holder.itemView.medDosage_txt_frame.visibility = View.GONE

            holder.itemView.indArrow.setImageResource(R.drawable.ic_arrow)
            holder.itemView.contrArrow.setImageResource(R.drawable.ic_arrow)
            holder.itemView.seArrow.setImageResource(R.drawable.ic_arrow)
            holder.itemView.dosArrow.setImageResource(R.drawable.ic_arrow)

            visible = if (holder.itemView.footer.isGone) View.VISIBLE else View.GONE
            holder.itemView.footer.visibility = visible
        }

        holder.itemView.medIndication_frame.setOnClickListener {
            val visible = if (holder.itemView.medIndication_txt_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medIndication_txt_frame.visibility = visible
            holder.itemView.medIndication_txt.text = currentItem.indication

            if (visible == View.VISIBLE) holder.itemView.indArrow.setImageResource(R.drawable.ic_arrow_up)
            else holder.itemView.indArrow.setImageResource(R.drawable.ic_arrow)
        }

        holder.itemView.medContraindication_frame.setOnClickListener {
            val visible = if (holder.itemView.medContraindication_txt_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medContraindication_txt_frame.visibility = visible
            holder.itemView.medContraindication_txt.text = currentItem.contraIndication

            if (visible == View.VISIBLE) holder.itemView.contrArrow.setImageResource(R.drawable.ic_arrow_up)
            else holder.itemView.contrArrow.setImageResource(R.drawable.ic_arrow)
        }

        holder.itemView.medSideEffects_frame.setOnClickListener {
            val visible = if (holder.itemView.medSideEffects_txt_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medSideEffects_txt_frame.visibility = visible
            holder.itemView.medSideEffects_txt.text = currentItem.sideEffects

            if (visible == View.VISIBLE) holder.itemView.seArrow.setImageResource(R.drawable.ic_arrow_up)
            else holder.itemView.seArrow.setImageResource(R.drawable.ic_arrow)
        }

        holder.itemView.medDosage_frame.setOnClickListener {
            val visible = if (holder.itemView.medDosage_txt_frame.isGone) View.VISIBLE else View.GONE
            holder.itemView.medDosage_txt_frame.visibility = visible
            holder.itemView.medDosage_txt.text = currentItem.dosage

            if (visible == View.VISIBLE) holder.itemView.dosArrow.setImageResource(R.drawable.ic_arrow_up)
            else holder.itemView.dosArrow.setImageResource(R.drawable.ic_arrow)
        }
    }

    override fun getItemCount(): Int {
        return medicineList.size
    }

    fun setData(medicine: List<Medicine>) {
        this.medicineList = medicine
        notifyDataSetChanged()
    }
}
package com.korashiGroup.salamtakPatient.adapter


import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.korashiGroup.salamtakPatient.R
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSpecialictyBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSpecialistBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData


class SpecialistAdapter(private val select:SelectSpecialist):ListAdapter<SpecialistData,SpecialistAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSpecialictyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.chExaminationtype.text=sp.name
        holder.binding.ivExaminationtype.load("https://salamtechapi.azurewebsites.net/${sp.image}") {
            crossfade(true)
            crossfade(1000)
            transformations(CircleCropTransformation())
        }
        holder.binding.chExaminationtype.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                select.onItemSelected(sp)
            }
        })
        holder.binding.chExaminationtype.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }
        holder.binding.chExaminationtype.setOnLongClickListener {
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
            holder.binding.layout.setBackgroundColor(getColor(holder.binding.tvExaminationtype.context,R.color.gray))
            true
        }

        holder.binding.chExaminationtype.isChecked = selectedPosition == position

        if (selectedPosition == position) {
        } else {
        }

    }

    class ViewHolder(itemBinding: ItemLayoutSpecialictyBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSpecialictyBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }


    interface SelectSpecialist{
        fun onItemSelected(specialist:SpecialistData)
    }


}
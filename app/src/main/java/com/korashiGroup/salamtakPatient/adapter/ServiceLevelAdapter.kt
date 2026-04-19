package com.korashiGroup.salamtakPatient.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView


import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSubspecialistBinding
import com.korashiGroup.salamtakPatient.model.nursingServices.service.ServiceLevelData

class ServiceLevelAdapter(private val serviceLevel:SelectServiceLevel):ListAdapter<ServiceLevelData,ServiceLevelAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view=ItemLayoutSubspecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.displayName

        holder.binding.radioSpecialist.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                serviceLevel.onItemSelectServiceLevel(sp)
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#00D2CB"))
            }else{
                holder.binding.radioSpecialist.setTextColor(Color.parseColor("#2B2979"))
            }
        })
        holder.binding.radioSpecialist.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }

        holder.binding.radioSpecialist.isChecked = selectedPosition == position
    }


    class ViewHolder(itemBinding: ItemLayoutSubspecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSubspecialistBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<ServiceLevelData>() {
        override fun areItemsTheSame(
            oldItem: ServiceLevelData,
            newItem: ServiceLevelData
        ): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(
            oldItem: ServiceLevelData,
            newItem: ServiceLevelData
        ): Boolean {
            return true
        }

    }

    interface SelectServiceLevel{
        fun onItemSelectServiceLevel(serviceLevelData: ServiceLevelData)
    }
}
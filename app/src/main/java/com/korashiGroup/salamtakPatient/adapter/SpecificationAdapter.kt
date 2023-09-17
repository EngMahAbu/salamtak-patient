package com.korashiGroup.salamtakPatient.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutCounteryBinding
import com.korashiGroup.salamtakPatient.databinding.ItemLayoutSpecialistBinding
import com.korashiGroup.salamtakPatient.model.SpecialistData
import com.korashiGroup.salamtakPatient.model.register.DataCountry


class SpecificationAdapter(private val select:SelectSpecification):ListAdapter<SpecialistData,SpecificationAdapter.ViewHolder>(DiffCallback()) {

    var selectedPosition=-1


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view=ItemLayoutSpecialistBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sp=getItem(position)

        holder.binding.radioSpecialist.text=sp.name

        holder.binding.radioSpecialist.setOnCheckedChangeListener(CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                select.onItemSelected(sp)
            }
        })
        holder.binding.radioSpecialist.setOnClickListener { view ->
            selectedPosition = holder.bindingAdapterPosition
            notifyDataSetChanged()
        }

        holder.binding.radioSpecialist.isChecked = selectedPosition == position

    }

    class ViewHolder(itemBinding: ItemLayoutSpecialistBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        internal val binding: ItemLayoutSpecialistBinding = itemBinding
    }


    private class DiffCallback : DiffUtil.ItemCallback<SpecialistData>() {
        override fun areItemsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return oldItem==newItem
        }

        override fun areContentsTheSame(oldItem: SpecialistData, newItem: SpecialistData): Boolean {
            return true
        }
    }


    interface SelectSpecification{
        fun onItemSelected(specialist:SpecialistData)
    }


}